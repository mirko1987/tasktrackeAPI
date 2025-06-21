// TaskServiceImpl.java
package com.tasktracker.service;

import com.tasktracker.dto.TaskCreateDto;
import com.tasktracker.dto.TaskResponseDto;
import com.tasktracker.model.Task;
import com.tasktracker.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TaskResponseDto> getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public TaskResponseDto createTask(TaskCreateDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        Task savedTask = taskRepository.save(task);
        return convertToDto(savedTask);
    }

    @Override
    public Optional<TaskResponseDto> completeTask(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setCompleted(true);
            Task updatedTask = taskRepository.save(task);
            return Optional.of(convertToDto(updatedTask));
        }
        return Optional.empty();
    }

    private TaskResponseDto convertToDto(Task task) {
        return new TaskResponseDto(task.getId(), task.getTitle(), task.getDescription(), task.getCompleted());
    }
}