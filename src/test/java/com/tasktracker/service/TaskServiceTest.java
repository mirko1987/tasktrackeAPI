// TaskServiceTest.java
package com.tasktracker.service;

import com.tasktracker.dto.TaskCreateDto;
import com.tasktracker.dto.TaskResponseDto;
import com.tasktracker.model.Task;
import com.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task1;
    private Task task2;
    private TaskResponseDto taskResponseDto1;
    private TaskResponseDto taskResponseDto2;

    @BeforeEach
    void setUp() {
        task1 = new Task("Test Task 1", "Description for test task 1");
        task1.setId(1L);
        task1.setCompleted(false);

        task2 = new Task("Test Task 2", "Description for test task 2");
        task2.setId(2L);
        task2.setCompleted(true);

        taskResponseDto1 = new TaskResponseDto(1L, "Test Task 1", "Description for test task 1", false);
        taskResponseDto2 = new TaskResponseDto(2L, "Test Task 2", "Description for test task 2", true);
    }

    @Test
    void getAllTasks_shouldReturnListOfTaskResponseDtos() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<TaskResponseDto> tasks = taskService.getAllTasks();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertEquals(taskResponseDto1.getTitle(), tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_shouldReturnTaskResponseDtoWhenFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        Optional<TaskResponseDto> foundTask = taskService.getTaskById(1L);

        assertTrue(foundTask.isPresent());
        assertEquals(taskResponseDto1.getTitle(), foundTask.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_shouldReturnEmptyWhenNotFound() {
        when(taskRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<TaskResponseDto> foundTask = taskService.getTaskById(3L);

        assertFalse(foundTask.isPresent());
        verify(taskRepository, times(1)).findById(3L);
    }

    @Test
    void createTask_shouldReturnCreatedTaskResponseDto() {
        TaskCreateDto newTaskDto = new TaskCreateDto();
        newTaskDto.setTitle("New Task");
        newTaskDto.setDescription("New Description");

        Task savedTask = new Task("New Task", "New Description");
        savedTask.setId(3L);
        savedTask.setCompleted(false);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponseDto createdTask = taskService.createTask(newTaskDto);

        assertNotNull(createdTask);
        assertEquals(savedTask.getTitle(), createdTask.getTitle());
        assertEquals(savedTask.getId(), createdTask.getId());
        assertFalse(createdTask.getCompleted());
        verify(taskRepository, times(1)).save(any(Task.class));
    }


    @Test
    void completeTask_shouldMarkTaskAsCompletedAndReturnDto() {
        Task originalTask = new Task("Task to complete", "Description");
        originalTask.setId(1L);
        originalTask.setCompleted(false);

        Task completedSimulatedTask = new Task("Task to complete", "Description");
        completedSimulatedTask.setId(1L);
        completedSimulatedTask.setCompleted(true);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(originalTask));
        when(taskRepository.save(any(Task.class))).thenReturn(completedSimulatedTask);

        Optional<TaskResponseDto> completedTaskDto = taskService.completeTask(1L);

        assertTrue(completedTaskDto.isPresent());
        assertTrue(completedTaskDto.get().getCompleted());
        assertEquals(1L, completedTaskDto.get().getId());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void completeTask_shouldReturnEmptyWhenTaskNotFound() {
        when(taskRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<TaskResponseDto> completedTask = taskService.completeTask(3L);

        assertFalse(completedTask.isPresent());
        verify(taskRepository, times(1)).findById(3L);
        verify(taskRepository, never()).save(any(Task.class));
    }
}