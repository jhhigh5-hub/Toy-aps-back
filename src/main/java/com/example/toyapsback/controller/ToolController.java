package com.example.toyapsback.controller;

import com.example.toyapsback.entity.Tool;
import com.example.toyapsback.repository.ToolRepository;
import com.example.toyapsback.request.ToolBulkUpsertRequest;
import com.example.toyapsback.response.ToolListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tools")
public class ToolController {
    private final ToolRepository toolRepository;

    @GetMapping
    public ResponseEntity<?> handleGetTools() {
        List<Tool> tools = toolRepository.findAll();
        long total = toolRepository.count();
        ToolListResponse response = ToolListResponse.builder().tools(tools).total(total).build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<?> handlePostTools(@RequestBody ToolBulkUpsertRequest toolBulkUpsertRequest) {
        List<Tool> savedTools = toolRepository.findAll();


        List<Tool> tools = toolBulkUpsertRequest.getTools().stream().map(
                one -> Tool.builder().id(one.getId())
                .name(one.getName()).description(one.getDescription()).build()).toList();
        toolRepository.saveAll(tools);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
