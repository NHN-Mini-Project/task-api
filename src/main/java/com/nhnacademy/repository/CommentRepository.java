package com.nhnacademy.repository;

import com.nhnacademy.model.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTaskId(long taskId);
    List<Comment> findCommentsByCommentWriterId(String commentWriterId);
}
