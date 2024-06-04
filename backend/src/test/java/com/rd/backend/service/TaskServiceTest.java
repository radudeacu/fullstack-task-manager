package com.rd.backend.service;

import com.rd.backend.model.Task;
import com.rd.backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    public TaskServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setTitle("Task 2");
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task();
        task.setTitle("Task 1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);
        assertEquals("Task 1", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        task.setTitle("Task 1");

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task);
        assertEquals("Task 1", result.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task();
        task.setTitle("Task 1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task taskDetails = new Task();
        taskDetails.setTitle("Updated Task");

        Task result = taskService.updateTask(1L, taskDetails);
        assertEquals("Updated Task", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task();
        task.setTitle("Task 1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).delete(task);
    }
}