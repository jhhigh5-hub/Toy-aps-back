package com.example.toyapsback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SolveApiResult {
    private int makespan;
    private String status;
    private List<TaskSchedule> schedules;

    @Setter
    @Getter
    public static class TaskSchedule {
        @JsonProperty("job_id")
        private String jobId;
        @JsonProperty("task_id")
        private String taskId;
        @JsonProperty("tool_id")
        private String toolId;

        private Integer start;
        private Integer end;
    }
}
