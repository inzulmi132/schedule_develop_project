package com.sparta.scheduledevelop.domain.comment.controller;

import com.sparta.scheduledevelop.domain.comment.dto.CommentRequestDto;
import com.sparta.scheduledevelop.domain.comment.dto.CommentResponseDto;
import com.sparta.scheduledevelop.domain.user.entity.User;
import com.sparta.scheduledevelop.domain.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "commentController")
@RestController
@RequestMapping("/api/schedules/{scheduleId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(HttpServletRequest request,
                                        @PathVariable Long scheduleId,
                                        @Valid CommentRequestDto requestDto,
                                        BindingResult bindingResult
    ) {
        if(validationCheck(bindingResult.getFieldErrors())) throw new RuntimeException("Validation Exception");
        User user = (User) request.getAttribute("user");
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
            HttpServletRequest request,
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @Valid CommentRequestDto requestDto,
            BindingResult bindingResult
    ) {
        if(validationCheck(bindingResult.getFieldErrors())) throw new RuntimeException("Validation Exception");
        User user = (User) request.getAttribute("user");
        CommentResponseDto responseDto = commentService.updateComment(user, scheduleId, commentId, requestDto);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(responseDto);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            HttpServletRequest request,
            @PathVariable Long scheduleId,
            @PathVariable Long commentId
    ) {
        User user = (User) request.getAttribute("user");
        commentService.deleteComment(user, scheduleId, commentId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Comment deleted Successfully");
    }

    public boolean validationCheck(List<FieldError> fieldErrors) {
        if(fieldErrors.isEmpty()) return false;
        for(FieldError fieldError : fieldErrors)
            log.error("{} 필드 : {}", fieldError.getField(), fieldError.getDefaultMessage());
        return true;
    }
}
