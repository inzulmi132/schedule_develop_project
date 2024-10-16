package com.sparta.scheduledevelop.controller;

import com.sparta.scheduledevelop.dto.CommentRequestDto;
import com.sparta.scheduledevelop.dto.CommentResponseDto;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "commentController")
@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{scheduleId}/write")
    public String createComment(HttpServletRequest request, @PathVariable Long scheduleId, @Valid CommentRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Error";
        User user = (User) request.getAttribute("user");
        return commentService.createComment(user, scheduleId, dto);
    }

    @GetMapping("/{scheduleId}")
    public List<CommentResponseDto> findComments(@PathVariable Long scheduleId) {
        return commentService.findComments(scheduleId).stream().map(CommentResponseDto::new).toList();
    }

    @PutMapping("/{commentId}/update")
    public String updateComment(HttpServletRequest request, @PathVariable Long commentId, @Valid CommentRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Error";
        User user = (User) request.getAttribute("user");
        return commentService.updateComment(user, commentId, dto);
    }

    @DeleteMapping("/{commentId}/delete")
    public String deleteComment(HttpServletRequest request, @PathVariable Long commentId) {
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
