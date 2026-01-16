package com.example.toyapsback.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TaskUpsertResponse {
    int created;
    int updated;
    int deleted;
}
