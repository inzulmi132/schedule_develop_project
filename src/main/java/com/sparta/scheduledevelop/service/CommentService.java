package com.sparta.scheduledevelop.service;

import com.sparta.scheduledevelop.dto.CommentRequestDto;
import com.sparta.scheduledevelop.entity.Comment;
import com.sparta.scheduledevelop.entity.Schedule;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.repository.CommentRepository;
import com.sparta.scheduledevelop.repository.ScheduleRepository;
import com.sparta.scheduledevelop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public String createComment(User creator, Long scheduleId, CommentRequestDto dto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        Comment comment = new Comment(dto.getText(), creator, schedule);
        schedule.getCommentList().add(comment);
        commentRepository.save(comment);
        return "Comment created";
    }

    public List<Comment> findComments(Long scheduleId) {
        return commentRepository.findAllByScheduleId(scheduleId);
    }

    @Transactional
    public String updateComment(User user, Long commentId, CommentRequestDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if(!Objects.equals(user.getId(), comment.getCommentCreator().getId()))
            return "You don't have permission to update this text";
        comment.setText(dto.getText());
        return "Comment updated";
    }

    @Transactional
    public String deleteComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if(!Objects.equals(user.getId(), comment.getCommentCreator().getId()))
            return "You don't have permission to delete this text";
        commentRepository.delete(comment);

        Schedule schedule = comment.getSchedule();
        schedule.getCommentList().remove(comment);
        user.getCommentList().remove(comment);

        return "Comment deleted";
    }
}
