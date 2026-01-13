package com.example.toyapsback.response;

import com.example.toyapsback.entity.Job;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class JobListResponse {
    List<Job> jobs;
}
