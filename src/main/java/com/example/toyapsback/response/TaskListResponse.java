package com.example.toyapsback.response;

import com.example.toyapsback.entity.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class TaskListResponse {
    List<Task> tasks;
}
