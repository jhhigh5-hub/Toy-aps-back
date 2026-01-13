package com.example.toyapsback.controller;

import com.example.toyapsback.entity.Job;
import com.example.toyapsback.repository.JobRepository;
import com.example.toyapsback.request.JobBulkUpsertRequest;
import com.example.toyapsback.response.JobListResponse;
import com.example.toyapsback.response.JobParseResponse;
import com.example.toyapsback.response.JobUpsertResponse;
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
@RequestMapping("/api/jobs")
public class JobController {

    private final JobRepository jobRepository;

    @PostMapping("/parse/xls")
    public ResponseEntity<?> handlePostParseXls(@RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
//        sheet.getFirstRowNum();
        Iterator<Row> iterator = sheet.rowIterator();
        Row header = iterator.next();
//        System.out.println(header.getCell(0).getStringCellValue());
//        System.out.println(header.getCell(1).getStringCellValue());
//        System.out.println(header.getCell(2).getStringCellValue());
//        System.out.println(header.getCell(3).getStringCellValue());
        DataFormatter formatter = new DataFormatter();
List<JobParseResponse.Item> items = new ArrayList<>();
        while (iterator.hasNext()) {
            Row row =  iterator.next();
//            System.out.println(formatter.formatCellValue(row.getCell(0)));
//            System.out.println(formatter.formatCellValue(row.getCell(1)));
            JobParseResponse.Item one =
                    JobParseResponse.Item.builder()
                            .id(formatter.formatCellValue(row.getCell(0)))
                            .name(formatter.formatCellValue(row.getCell(1)))
                            .description(formatter.formatCellValue(row.getCell(2)))
                            .active(Boolean.parseBoolean(formatter.formatCellValue(row.getCell(3))))
                            .build();
            items.add(one);
        }
        JobParseResponse response = JobParseResponse.builder().items(items).build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> handlePostJobs(@RequestBody JobBulkUpsertRequest jobBulkUpsertRequest) {
        List<JobBulkUpsertRequest.JobUpsertItem> items = jobBulkUpsertRequest.getJobs();
        List<String> itemIds = items.stream().map(e -> e.getId()).toList();
        // 요청 객체 자체는 비슷하다고 생각했을 때,,,, 빼고 넘기면 지워버릴거임.
        List<Job> savedJobs = jobRepository.findAll();
        List<Job> notContainsJobs = savedJobs.stream().filter(e -> !itemIds.contains(e.getId())
        ).toList();

        jobRepository.deleteAll(notContainsJobs);

        List<Job> upsertJobs = items.stream().map(e-> Job.builder().id(e.getId()).name(e.getName())
                .description(e.getDescription()).active(e.isActive()).build()).toList();
        jobRepository.saveAll(upsertJobs);

        int deleted = notContainsJobs.size();
        int updated = savedJobs.size() - deleted;
        int created = upsertJobs.size() - updated;

        JobUpsertResponse response = JobUpsertResponse.builder().deleted(deleted).updated(updated).created(created).build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> handleGetJobs() {
        List<Job> jobs = jobRepository.findAll();
        JobListResponse response = JobListResponse.builder().jobs(jobs).build();
        return ResponseEntity.ok(response);
    }
}
