package com.nhnacademy.service.impl;

import com.nhnacademy.exception.comment.CommentNotFoundException;
import com.nhnacademy.model.comment.dto.RegisterCommentRequest;
import com.nhnacademy.model.comment.dto.UpdateCommentRequest;
import com.nhnacademy.model.comment.entity.Comment;
import com.nhnacademy.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentServiceImpl(commentRepository);
    }

    @Test
    void registerComment_SaveComment() {
        RegisterCommentRequest request = new RegisterCommentRequest("댓글 내용", 1L, "123");

        commentService.registerComment(request);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void updateComment_UpdateComment() {
        long commentId = 1L;
        UpdateCommentRequest request = new UpdateCommentRequest("수정된 내용");
        Comment comment = new Comment("원래 내용", 1L, "123");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.updateComment(commentId, request);

        assertEquals("수정된 내용", comment.getCommentContent());
        verify(commentRepository).save(comment);
    }

    @Test
    void deleteComment_DeleteComment() {
        long commentId = 1L;
        Comment comment = new Comment("댓글 내용", 1L, "123");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        commentService.deleteComment(commentId);

        verify(commentRepository).delete(comment);
    }

    @Test
    void getCommentById() {
        long commentId = 1L;
        Comment comment = new Comment("댓글 내용", 1L, "123");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Comment result = commentService.getCommentById(commentId);

        assertEquals(comment, result);
    }

    @Test
    void getCommentById_NotFound() {
        long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.getCommentById(commentId));
    }

    @Test
    void getCommentsByTaskId() {
        long taskId = 1L;
        List<Comment> comments = List.of(new Comment("댓글 내용", taskId, "123"));

        when(commentRepository.findAllByTaskId(taskId)).thenReturn(comments);

        List<Comment> result = commentService.getCommentsByTaskId(taskId);

        assertEquals(comments, result);
    }
}