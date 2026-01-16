package com.example.toyapsback.request;

import com.example.toyapsback.entity.Job;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class ScenarioCreateRequest {
    private String description;
    private LocalDateTime scheduleAt;
    private List<String> jobIds;
}
