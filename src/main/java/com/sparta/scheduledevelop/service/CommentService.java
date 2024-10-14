package com.sparta.scheduledevelop.service;

import com.sparta.scheduledevelop.dto.CommentRequestDto;
import com.sparta.scheduledevelop.dto.CommentResponseDto;
import com.sparta.scheduledevelop.entity.Comment;
import com.sparta.scheduledevelop.entity.Schedule;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.repository.CommentRepository;
import com.sparta.scheduledevelop.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    public CommentService(CommentRepository commentRepository, ScheduleRepository scheduleRepository) {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public String createComment(User user, Long scheduleId, CommentRequestDto dto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        Comment comment = new Comment(dto.getText(), user, schedule);
        schedule.getCommentList().add(comment);
        commentRepository.save(comment);
        return "Comment created";
    }

    public List<CommentResponseDto> findComments(Long scheduleId) {
        return commentRepository.findAllByScheduleId(scheduleId).stream().map(CommentResponseDto::new).toList();
    }

    public String updateComment(User user, Long commentId, CommentRequestDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if(!Objects.equals(user.getId(), comment.getCreator().getId())) return "You don't have permission to update this text";
        comment.setText(dto.getText());
        commentRepository.save(comment);
        return "Comment updated";
    }

    public String deleteComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if(!Objects.equals(user.getId(), comment.getCreator().getId())) return "You don't have permission to delete this text";
        commentRepository.delete(comment);
        return "Comment deleted";
    }
}
