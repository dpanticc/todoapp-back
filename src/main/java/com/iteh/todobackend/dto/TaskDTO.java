package com.iteh.todobackend.dto;

import java.util.Date;

public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private boolean completed;
    private Date dueDate;
    private String priority;
    private Long userId;

    public TaskDTO() {
    }

    public TaskDTO(Long id, String name, String description, boolean completed, Date dueDate, String priority, Long userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
        this.priority = priority;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
