package com.entities;

import com.enums.ScrapingTaskStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ScrapingTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String movieTitle;

    @Enumerated(EnumType.STRING)
    private ScrapingTaskStatus status;

    @Column(columnDefinition = "TEXT")
    private String result;
}