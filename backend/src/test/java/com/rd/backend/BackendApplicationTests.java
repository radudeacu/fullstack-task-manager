package com.rd.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rd.backend.model.Task;
import com.rd.backend.repository.TaskRepository;
import com.rd.backend.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class BackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskRepository taskRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testGetAllTasks() throws Exception {
		mockMvc.perform(get("/api/tasks")).andExpect(status().isOk());
	}

	@Test
	void testCreateTask() throws Exception {
		Task task = new Task();
		task.setTitle("New Task");

		mockMvc.perform(post("/api/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("New Task"));
	}

	@Test
	void testGetTaskById() throws Exception {
		Task task = new Task();
		task.setTitle("Task to Get");
		task = taskRepository.save(task);

		mockMvc.perform(get("/api/tasks/" + task.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Task to Get"));

		taskRepository.delete(task);
	}

	@Test
	void testUpdateTask() throws Exception {
		Task task = new Task();
		task.setTitle("Task to Update");
		task = taskRepository.save(task);

		Task updatedTask = new Task();
		updatedTask.setTitle("Updated Task");

		mockMvc.perform(put("/api/tasks/" + task.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedTask)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Updated Task"));

		taskRepository.delete(task);
	}

	@Test
	void testDeleteTask() throws Exception {
		Task task = new Task();
		task.setTitle("Task to Delete");
		task = taskRepository.save(task);

		mockMvc.perform(delete("/api/tasks/" + task.getId()))
				.andExpect(status().isOk());

		assertThat(taskRepository.findById(task.getId())).isEmpty();
	}

}
