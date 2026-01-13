package com.example.toyapsback.response;

import com.example.toyapsback.entity.Tool;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ToolListResponse {
    private long total;
    private List<Tool> tools;
}
