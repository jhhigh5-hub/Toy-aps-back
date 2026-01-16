package com.example.toyapsback.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class TaskParseResponse {
    List<Item> items;

    @Getter
    @Builder
    public static class Item {
        private String id;
        private String jobId;
        private int seq;
        private String name;
        private String description;
        private String toolId;
        private int duration;
    }
}
