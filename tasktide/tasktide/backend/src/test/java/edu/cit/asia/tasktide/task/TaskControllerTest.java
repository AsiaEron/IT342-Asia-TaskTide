package edu.cit.asia.tasktide.task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.cit.asia.tasktide.task.entity.TaskModel;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddTask_Success() throws Exception {
        TaskModel task = new TaskModel();
        task.setTask_name("Test Task");
        task.setDescription("Test Description");
        task.setEnergy_level("Medium");
        task.setStatus("ACTIVE");

        TaskModel savedTask = new TaskModel();
        savedTask.setTask_id(1);
        savedTask.setTask_name("Test Task");

        when(taskService.addTask(any(TaskModel.class))).thenReturn(savedTask);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task_id").value(1))
                .andExpect(jsonPath("$.task_name").value("Test Task"));
    }

    @Test
    public void testGetUserTasks_Success() throws Exception {
        TaskModel task1 = new TaskModel();
        task1.setTask_id(1);
        task1.setTask_name("Task 1");

        TaskModel task2 = new TaskModel();
        task2.setTask_id(2);
        task2.setTask_name("Task 2");

        List<TaskModel> tasks = Arrays.asList(task1, task2);

        when(taskService.getUserTasks(anyInt())).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].task_id").value(1))
                .andExpect(jsonPath("$[1].task_id").value(2));
    }

    @Test
    public void testEditTask_Success() throws Exception {
        TaskModel task = new TaskModel();
        task.setTask_name("Updated Task");

        TaskModel updatedTask = new TaskModel();
        updatedTask.setTask_id(1);
        updatedTask.setTask_name("Updated Task");

        when(taskService.editTask(anyInt(), any(TaskModel.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task_id").value(1))
                .andExpect(jsonPath("$.task_name").value("Updated Task"));
    }

    @Test
    public void testDeleteTask_Success() throws Exception {
        doNothing().when(taskService).deleteTask(anyInt());

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully."));
    }
}