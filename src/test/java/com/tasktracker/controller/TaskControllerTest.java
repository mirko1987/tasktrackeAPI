// TaskControllerTest.java
package com.tasktracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasktracker.dto.TaskCreateDto;
import com.tasktracker.dto.TaskResponseDto;
import com.tasktracker.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskResponseDto taskResponseDto1;
    private TaskResponseDto taskResponseDto2;

    @BeforeEach
    void setUp() {
        taskResponseDto1 = new TaskResponseDto(1L, "Task 1", "Description 1", false);
        taskResponseDto2 = new TaskResponseDto(2L, "Task 2", "Description 2", true);
    }

    @Test
    void getAllTasks_shouldReturnListOfTaskResponseDtos() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(taskResponseDto1, taskResponseDto2));

        mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is(taskResponseDto1.getTitle())))
                .andExpect(jsonPath("$[1].title", is(taskResponseDto2.getTitle())));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void createTask_shouldReturnCreatedTaskResponseDto() throws Exception {
        TaskCreateDto newTaskDto = new TaskCreateDto();
        newTaskDto.setTitle("New Task");
        newTaskDto.setDescription("New Description");

        TaskResponseDto createdTaskResponse = new TaskResponseDto(3L, "New Task", "New Description", false);
        when(taskService.createTask(any(TaskCreateDto.class))).thenReturn(createdTaskResponse);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTaskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is(newTaskDto.getTitle())));

        verify(taskService, times(1)).createTask(any(TaskCreateDto.class));
    }

    @Test
    void createTask_shouldReturnBadRequestWhenTitleIsEmpty() throws Exception {
        TaskCreateDto invalidTaskDto = new TaskCreateDto();
        invalidTaskDto.setTitle("");
        invalidTaskDto.setDescription("Description");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTaskDto)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any(TaskCreateDto.class));
    }

    @Test
    void completeTask_shouldReturnCompletedTaskResponseDto() throws Exception {
        TaskResponseDto completedTaskResponse = new TaskResponseDto(1L, "Task 1", "Description 1", true);
        when(taskService.completeTask(1L)).thenReturn(Optional.of(completedTaskResponse));

        mockMvc.perform(put("/tasks/{id}/complete", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.completed", is(true)));

        verify(taskService, times(1)).completeTask(1L);
    }

    @Test
    void completeTask_shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        when(taskService.completeTask(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/tasks/{id}/complete", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).completeTask(99L);
    }
}