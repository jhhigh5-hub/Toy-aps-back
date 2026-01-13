package com.example.toyapsback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    String id;
    String name;
    String description;
    boolean active;
}
