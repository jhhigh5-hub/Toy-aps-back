package com.example.toyapsback.controller;

import com.example.toyapsback.dto.SolveApiResult;
import com.example.toyapsback.entity.Scenario;
import com.example.toyapsback.entity.ScenarioJob;
import com.example.toyapsback.entity.ScenarioSchedule;
import com.example.toyapsback.repository.*;
import com.example.toyapsback.request.ScenarioCreateRequest;
import com.example.toyapsback.response.ScenarioListResponse;
import com.example.toyapsback.response.ScenarioScheduleListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scenarios")
public class ScenarioController {

    private final ScenarioJobRepository scenarioJobRepository;
    private final ScenarioRepository scenarioRepository;
    private final JobRepository jobRepository;
    private final TaskRepository taskRepository;
    private final ScenarioScheduleRepository scenarioScheduleRepository;

    @PostMapping
    public ResponseEntity<?> handlePostScenario(@RequestBody ScenarioCreateRequest scenarioCreateRequest) {
        Scenario scenario = Scenario.builder()
                .description(scenarioCreateRequest.getDescription())
                .scheduleAt(scenarioCreateRequest.getScheduleAt())
                .build();

        scenarioRepository.save(scenario);

        List<ScenarioJob> scenarioJobs = scenarioCreateRequest.getJobIds().stream()
                .map(one -> ScenarioJob.builder()
                        .scenario(scenario)
                        .job(jobRepository.findById(one).orElseThrow())
                        .build()).toList();
        scenarioJobRepository.saveAll(scenarioJobs);
        Map<String, Object> response = Map.of("created", scenario);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/{scenarioId}")
//    public ResponseEntity<?> handleGetScenarios(@PathVariable String scenarioId) {
//
//        Scenario scenario = scenarioRepository.findById(scenarioId).orElseThrow();
//        Map<String, Object> response = Map.of("scenario", scenario);
//        return ResponseEntity.ok(response);
//    }

    // python으로 보내주는 작업
    @PostMapping("/{scenarioId}/simulate")
    public ResponseEntity<?> handlePostScenarioSimulate(@PathVariable String scenarioId) {

        Scenario scenario = scenarioRepository.findById(scenarioId).orElseThrow();
        RestClient restClient = RestClient.create();

        SolveApiResult result = restClient.post().uri("http://127.0.0.1:5000/api/solve")
                .body(scenario).retrieve().body(SolveApiResult.class);
//        System.out.println("solved: " + result);

        // 파이썬에게 맡기고, 받아온 응답을 잘 파싱해서 DB에 insert해야함
        scenario.setStatus(result.getStatus());
        scenario.setWorkTime(result.getMakespan());
        scenarioRepository.save(scenario);
        // 그 다음에, schedule을 등록하면 된다.
        List<ScenarioSchedule> schedules = result.getSchedules().stream().map(one -> {
            return ScenarioSchedule.builder().scenario(scenario)
                    .task(taskRepository.findById(one.getTaskId()).orElseThrow())
                    .startAt(scenario.getScheduleAt().plusHours(one.getStart()))
                    .endAt(scenario.getScheduleAt().plusHours(one.getEnd()))
                    .build();
        }).toList();
        scenarioScheduleRepository.saveAll(schedules);

        Map<String, Object> response = Map.of("status", result.getStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{scenarioId}/json")
    public ResponseEntity<?> handleGetScenarioJson(@PathVariable String scenarioId) {

        Scenario scenario = scenarioRepository.findById(scenarioId).orElseThrow();

        return ResponseEntity.ok(scenario);
    }

    @GetMapping
    public ResponseEntity<?> handleGetAllScenarios() {
        List<Scenario> scenarios = scenarioRepository.findAll();
        List<ScenarioListResponse.Item> items = scenarios.stream().map(one -> ScenarioListResponse.Item.builder()
                .id(one.getId())
                .description(one.getDescription())
                .status(one.getStatus())
                .jobCount(one.getScenarioJobs().size())
                .build()).toList();
//        Map<String, Object> response = Map.of("scenarios", scenarios);
        ScenarioListResponse response = ScenarioListResponse.builder().scenario(items).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{scenarioId}/simulate")
    public ResponseEntity<?> handleGetScenarioSimulate(@PathVariable String scenarioId) throws IllegalAccessException {
        Scenario scenario = scenarioRepository.findById(scenarioId).orElseThrow(NoSuchElementException::new);
        if (scenario.getStatus().equals("READY")) {
            throw new IllegalAccessException();
        }

        List<ScenarioSchedule> allSchedules = scenarioScheduleRepository.findAll();
        List<ScenarioSchedule> selectedSchedules =
                allSchedules.stream().filter(one -> one.getScenario().equals(scenario)).toList();
        List<ScenarioScheduleListResponse.Item> items =
                selectedSchedules.stream().map(ScenarioScheduleListResponse.Item::fromEntity).toList();

        Map<String, Object> response = Map.of("status", scenario.getStatus(), "schedules", items);
        return ResponseEntity.ok(response);
    }
}
