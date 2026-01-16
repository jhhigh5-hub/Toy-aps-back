package com.example.toyapsback.response;

import com.example.toyapsback.entity.ScenarioSchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class ScenarioScheduleListResponse {

    @Builder
    @Getter
    public static class Item {
        private long id;
        private String taskId;
        private String taskName;
        private String taskDescription;
        private int taskSeq;
        private String jobId;
        private String jobName;
        private String jobDescription;
        private String toolId;
        private String toolName;
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        public static Item fromEntity(ScenarioSchedule s) {
            return Item.builder().id(s.getId()).taskId(s.getTask().getId()).taskName(s.getTask().getName()) //
                    .taskDescription(s.getTask().getDescription()) //
                    .taskSeq(s.getTask().getSeq()) //
                    .jobId(s.getTask().getJob().getId()) //
                    .jobName(s.getTask().getJob().getName()) //
                    .jobDescription(s.getTask().getJob().getDescription()) //
                    .toolId(s.getTask().getTool() == null ? null : s.getTask().getTool().getId()) //
                    .toolName(s.getTask().getTool() == null ? null : s.getTask().getTool().getName()) //
                    .startAt(s.getStartAt()).endAt(s.getEndAt()).build();
        }
    }
}