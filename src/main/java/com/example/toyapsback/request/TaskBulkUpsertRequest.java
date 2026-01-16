package com.example.toyapsback.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TaskBulkUpsertRequest {

    private List<TaskUpsertItem> tasks;

    @Setter
    @Getter
    public static class TaskUpsertItem {
        String id;
        String jobId;
        int seq;
        String name;
        String description;
        String toolId;
        int duration;
    }
}
