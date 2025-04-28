package com.nhnacademy.model.comment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    @NotNull
    @Setter
    private String commentContent;

    @NotNull
    private long taskId;

    @NotNull
    private String commentWriterId;

    public Comment(String commentContent, long taskId, String commentWriterId) {
        this.commentContent = commentContent;
        this.taskId = taskId;
        this.commentWriterId = commentWriterId;
    }
}
