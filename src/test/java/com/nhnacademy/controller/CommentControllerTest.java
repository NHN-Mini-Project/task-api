package com.nhnacademy.controller;

import com.nhnacademy.model.comment.dto.RegisterCommentRequest;
import com.nhnacademy.model.comment.dto.UpdateCommentRequest;
import com.nhnacademy.model.comment.entity.Comment;
import com.nhnacademy.service.CommentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    void testGetCommentByCommentId() throws Exception {
        Long commentId = 1L;
        Long projectId = 1L;
        Long taskId = 1L;

        Comment comment = new Comment("Test Comment Content", taskId, "123");

        when(commentService.getCommentById(commentId)).thenReturn(comment);

        mockMvc.perform(get("/project/{projectId}/task/{taskId}/comment/{commentId}", projectId, taskId, commentId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentContent").value("Test Comment Content"));
    }

    @Test
    void testGetCommentsByTaskId() throws Exception {
        Long taskId = 1L;

        when(commentService.getCommentsByTaskId(taskId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/project/1/task/{taskId}/comment", taskId))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterComment() throws Exception {
        Long projectId = 1L;
        Long taskId = 1L;

        mockMvc.perform(post("/project/{projectId}/task/{taskId}/comment", projectId, taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": \"Test Comment\"}"))
                .andExpect(status().isOk());

        Mockito.verify(commentService).registerComment(Mockito.any(RegisterCommentRequest.class));
    }

    @Test
    void testUpdateComment() throws Exception {
        Long commentId = 1L;

        mockMvc.perform(put("/project/1/task/1/comment/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": \"Updated Comment\"}"))
                .andExpect(status().isOk());

        Mockito.verify(commentService).updateComment(Mockito.eq(commentId), Mockito.any(UpdateCommentRequest.class));
    }

    @Test
    void testDeleteComment() throws Exception {
        Long commentId = 1L;

        mockMvc.perform(delete("/project/1/task/1/comment/{commentId}", commentId))
                .andExpect(status().isOk());

        Mockito.verify(commentService).deleteComment(commentId);
    }
}