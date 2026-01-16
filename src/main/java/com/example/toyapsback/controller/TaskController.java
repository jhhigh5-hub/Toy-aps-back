package com.example.toyapsback.controller;

import com.example.toyapsback.entity.Job;
import com.example.toyapsback.entity.Task;
import com.example.toyapsback.repository.JobRepository;
import com.example.toyapsback.repository.TaskRepository;
import com.example.toyapsback.repository.ToolRepository;
import com.example.toyapsback.request.JobBulkUpsertRequest;
import com.example.toyapsback.request.TaskBulkUpsertRequest;
import com.example.toyapsback.response.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final ToolRepository toolRepository;
    private final JobRepository jobRepository;

    @PostMapping("/parse/xls")
    public ResponseEntity<?> handlePostParseXls(@RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> iterator = sheet.rowIterator();
        Row header = iterator.next();

        DataFormatter formatter = new DataFormatter();
        List<TaskParseResponse.Item> items = new ArrayList<>();
        while (iterator.hasNext()) {
            Row row =  iterator.next();

            TaskParseResponse.Item one =
                    TaskParseResponse.Item.builder()
                            .id(formatter.formatCellValue(row.getCell(0)))
                            .jobId(formatter.formatCellValue(row.getCell(1)))
                            .seq(Integer.parseInt(formatter.formatCellValue(row.getCell(2))))
                            .name(formatter.formatCellValue(row.getCell(3)))
                            .description(formatter.formatCellValue(row.getCell(4)))
                            .toolId(formatter.formatCellValue(row.getCell(5)))
                            .duration(Integer.parseInt(formatter.formatCellValue(row.getCell(6))))
                            .build();
            items.add(one);
        }
        TaskParseResponse response = TaskParseResponse.builder().items(items).build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> handlePostTasks(@RequestBody TaskBulkUpsertRequest taskBulkUpsertRequest) {
        List<TaskBulkUpsertRequest.TaskUpsertItem> items = taskBulkUpsertRequest.getTasks();
        List<String> itemIds = items.stream().map(e -> e.getId()).toList();

        List<Task> savedTasks = taskRepository.findAll();
        List<Task> notContainsTasks = savedTasks.stream().filter(e -> !itemIds.contains(e.getId())
        ).toList();

        taskRepository.deleteAll(notContainsTasks);

        List<Task> upsertTasks = items.stream().map(e-> Task.builder()
                .id(e.getId())
                .job(jobRepository.findById(e.getJobId()).orElseThrow())
                .seq(e.getSeq())
                .name(e.getName())
                .description(e.getDescription())
                .tool(toolRepository.findById(e.getToolId()).orElseGet(()-> null))
                .duration(e.getDuration())
                .build()).toList();
        taskRepository.saveAll(upsertTasks);

//        int deleted = notContainsTasks.size();
//        int updated = savedTasks.size() - deleted;
//        int created = upsertTasks.size() - updated;
//
//        TaskUpsertResponse response = TaskUpsertResponse.builder().deleted(deleted).updated(updated).created(created).build();

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> handleGetTasks() {
        List<Task> tasks = taskRepository.findAll();

        TaskListResponse response = TaskListResponse.builder().tasks(tasks).build();
        return ResponseEntity.ok(response);
    }

}
