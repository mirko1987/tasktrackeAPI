// TaskService.java
package com.tasktracker.service;

import com.tasktracker.dto.TaskCreateDto;
import com.tasktracker.dto.TaskResponseDto;
import com.tasktracker.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<TaskResponseDto> getAllTasks();
    Optional<TaskResponseDto> getTaskById(Long id);
    TaskResponseDto createTask(TaskCreateDto taskDto);
    Optional<TaskResponseDto> completeTask(Long id);
}