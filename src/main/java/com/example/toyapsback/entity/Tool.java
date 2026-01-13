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
public class Tool {
    @Id
    private String id;
    private String name;
    private String description;
}
