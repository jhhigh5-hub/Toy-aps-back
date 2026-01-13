package com.example.toyapsback.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JobBulkUpsertRequest {

    private List<JobUpsertItem> jobs;

    @Setter
    @Getter
    public static class JobUpsertItem {
        String id;
        String name;
        String description;
        boolean active;
    }
}
