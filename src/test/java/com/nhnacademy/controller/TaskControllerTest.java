package com.nhnacademy.controller;

import com.nhnacademy.model.task.dto.TaskRegisterRequest;
import com.nhnacademy.model.task.dto.TaskUpdateRequest;
import com.nhnacademy.model.task.entity.Task;
import com.nhnacademy.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;



    @Test
    void testRegisterTask() throws Exception {
        long projectId = 1L;

        TaskRegisterRequest request = new TaskRegisterRequest("New Task", "Task description", null);

        Task mockTask = new Task("New Task", "Task description", null, null);

        java.lang.reflect.Field field = Task.class.getDeclaredField("taskId");
        field.setAccessible(true);
        field.set(mockTask, 1L);

        when(taskService.save(any(TaskRegisterRequest.class), eq(projectId))).thenReturn(mockTask);

        mockMvc.perform(post("/project/{projectId}/task", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "taskTitle": "New Task",
                                "taskDescription": "Task description"
                            }
                            """))
                .andExpect(status().isCreated());

        verify(taskService).save(any(TaskRegisterRequest.class), eq(projectId));
    }

    @Test
    void testUpdateTask() throws Exception {
        Task mockTask = new Task("New Task", "Task description", null, null);

        long projectId = 1L;
        long taskId = 1L;

        TaskUpdateRequest request = new TaskUpdateRequest("Updated Task", "Updated description");

        when(taskService.updateTask(taskId, request)).thenReturn(mockTask);

        mockMvc.perform(put("/project/{projectId}/task/{taskId}", projectId, taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "taskTitle": "Updated Task",
                                    "taskDescription": "Updated description"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("Task updated"));

        ArgumentCaptor<TaskUpdateRequest> captor = ArgumentCaptor.forClass(TaskUpdateRequest.class);
        verify(taskService).updateTask(eq(taskId), captor.capture());

        TaskUpdateRequest capturedRequest = captor.getValue();
        assert capturedRequest.getTaskTitle().equals("Updated Task");
        assert capturedRequest.getTaskDescription().equals("Updated description");
    }

    @Test
    void testDeleteTask() throws Exception {
        long projectId = 1L;
        long taskId = 1L;

        doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(delete("/project/{projectId}/task/{taskId}", projectId, taskId))
                .andExpect(status().isOk())
                .andExpect(content().string("task deleted"));

        verify(taskService).deleteTask(taskId);
    }
}
