package com.sparta.scheduledevelop.domain.comment.controller;

import com.sparta.scheduledevelop.domain.comment.dto.CommentRequestDto;
import com.sparta.scheduledevelop.domain.comment.dto.CommentResponseDto;
import com.sparta.scheduledevelop.domain.comment.service.CommentService;
import com.sparta.scheduledevelop.domain.common.annotation.LoginUser;
import com.sparta.scheduledevelop.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "commentController")
@RestController
@RequestMapping("/api/schedules/{scheduleId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @LoginUser User user,
            @PathVariable Long scheduleId,
            @Valid CommentRequestDto requestDto
    ) {
        CommentResponseDto responseDto = commentService.createComment(user, scheduleId, requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @GetMapping
    public List<CommentResponseDto> findComments(@PathVariable Long scheduleId) {
        return commentService.findComments(scheduleId);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto>  updateComment(
            @LoginUser User user,
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @Valid CommentRequestDto requestDto
    ) {
        CommentResponseDto responseDto = commentService.updateComment(user, scheduleId, commentId, requestDto);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(responseDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @LoginUser User user,
            @PathVariable Long scheduleId,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(user, scheduleId, commentId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Comment deleted Successfully");
    }
}
