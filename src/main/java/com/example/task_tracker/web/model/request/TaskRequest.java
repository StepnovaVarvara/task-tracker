package com.example.task_tracker.web.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    // TODO перепроверить форму ввода данных

    private String name;
    private String description;
    private String authorId;
    private String assigneeId;
    private Set<String> observerIds;
    // TODO добавить наблюдателей
}
