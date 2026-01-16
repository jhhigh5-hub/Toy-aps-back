package com.example.toyapsback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario {
    @Id
    private String id;
    private String description;
    private String status;
    private LocalDateTime scheduleAt;
    private Integer workTime;

    @OneToMany(mappedBy = "scenario")
    private List<ScenarioJob> scenarioJobs;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.status = "READY";
    }
}
