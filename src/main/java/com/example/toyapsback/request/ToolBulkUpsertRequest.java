package com.example.toyapsback.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ToolBulkUpsertRequest {

    private List<ToolUpsertItem> tools;

    @Setter
    @Getter
    public static class ToolUpsertItem {
        String id;
        String name;
        String description;
    }
}
