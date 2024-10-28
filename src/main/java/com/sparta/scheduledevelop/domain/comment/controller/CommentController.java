package com.sparta.scheduledevelop.domain.comment.controller;

import com.sparta.scheduledevelop.domain.comment.dto.CommentRequestDto;
import com.sparta.scheduledevelop.domain.comment.dto.CommentResponseDto;
import com.sparta.scheduledevelop.domain.user.entity.User;
import com.sparta.scheduledevelop.domain.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String createComment(HttpServletRequest request,
                                @PathVariable Long scheduleId,
                                @Valid CommentRequestDto dto,
                                BindingResult bindingResult
    ) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Error";
        User user = (User) request.getAttribute("user");
        return commentService.createComment(user, scheduleId, dto);
    }

    @GetMapping
    public List<CommentResponseDto> findComments(@PathVariable Long scheduleId) {
        return commentService.findComments(scheduleId).stream().map(CommentResponseDto::new).toList();
    }

    @PutMapping("/{commentId}")
    public String updateComment(
            HttpServletRequest request,
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @Valid CommentRequestDto dto,
            BindingResult bindingResult
    ) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Error";
        User user = (User) request.getAttribute("user");
        return commentService.updateComment(user, commentId, dto);
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(
            HttpServletRequest request,
            @PathVariable Long scheduleId,
            @PathVariable Long commentId
    ) {
        User user = (User) request.getAttribute("user");
        return commentService.deleteComment(user, commentId);
    }

    public boolean validationCheck(List<FieldError> fieldErrors) {
        if(fieldErrors.isEmpty()) return false;
        for(FieldError fieldError : fieldErrors)
            log.error("{} 필드 : {}", fieldError.getField(), fieldError.getDefaultMessage());
        return true;
    }
}
