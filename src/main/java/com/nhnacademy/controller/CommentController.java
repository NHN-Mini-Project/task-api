package com.nhnacademy.controller;

import com.nhnacademy.model.comment.dto.RegisterCommentRequest;
import com.nhnacademy.model.comment.dto.ResponseCommentListDto;
import com.nhnacademy.model.comment.dto.UpdateCommentRequest;
import com.nhnacademy.model.comment.entity.Comment;
import com.nhnacademy.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/project")
public class CommentController {

    private final CommentService commentService;

    // task의 해당 comment 반환
    @GetMapping("/{projectId}/task/{taskId}/comment/{commentId}")
    public Comment getCommentByCommentId(@PathVariable Long projectId,
                                     @PathVariable Long taskId,
                                     @PathVariable Long commentId) {
        return commentService.getCommentById(commentId);

    }
    
    
    // task의 comment 리스트 반환
    @GetMapping("/{projectId}/task/{taskId}/comment")
    public ResponseCommentListDto getCommentsByTaskId(@PathVariable("taskId") Long taskId,
                                                      @PathVariable("projectId") long projectId) {
        List<Comment> comments = commentService.getCommentsByTaskId(taskId);

        List<RegisterCommentRequest> list = new ArrayList<>();
        for(int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            list.add(new RegisterCommentRequest(comment.getCommentContent(), comment.getTaskId(), comment.getCommentWriterId()));
        }

        return new ResponseCommentListDto(list);
    }

    // Comment 등록
    @PostMapping("/{projectId}/task/{taskId}/comment")
    public void registerComment(@PathVariable Long projectId,
                                @PathVariable Long taskId,
                                @RequestBody RegisterCommentRequest request) {



        commentService.registerComment(request);
    }
    
    // Comment 수정
    @PutMapping("/{projectId}/task/{taskId}/comment/{commentId}")
    public void updateComment(@PathVariable long commentId,
                                   @RequestBody UpdateCommentRequest request) {
        commentService.updateComment(commentId, request);
    }
    
    // Comment 삭제
    @DeleteMapping("/{projectId}/task/{taskId}/comment/{commentId}")
    public void deleteComment(@PathVariable long commentId) {
        commentService.deleteComment(commentId);
    }

}
