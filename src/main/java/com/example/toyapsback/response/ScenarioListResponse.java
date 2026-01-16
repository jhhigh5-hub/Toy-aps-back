package com.example.toyapsback.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ScenarioListResponse {
    List<?> scenario;

    @Setter
    @Getter
    @Builder
    public static class Item {
        String id;
        String description;
        String status;
        int jobCount;
    }
}
