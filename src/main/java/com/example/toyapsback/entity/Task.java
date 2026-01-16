package com.example.toyapsback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    private String id;

    @JsonIgnore
    @ManyToOne
    private Job job;

    private int seq;
    private String name;
    private String description;

    @ManyToOne
    private Tool tool;
    private int duration;
}
