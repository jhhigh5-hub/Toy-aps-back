package com.example.toyapsback.response;

import com.example.toyapsback.entity.Job;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class JobParseResponse {
    List<Item> items;

    @Getter
    @Builder
    public  static class Item {
        private String id;
        private String name;
        private String description;
        private boolean active;
    }
}
