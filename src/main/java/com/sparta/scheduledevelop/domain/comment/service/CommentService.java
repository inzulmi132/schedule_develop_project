package com.sparta.scheduledevelop.domain.comment.service;

import com.sparta.scheduledevelop.domain.comment.dto.CommentRequestDto;
import com.sparta.scheduledevelop.domain.comment.dto.CommentResponseDto;
import com.sparta.scheduledevelop.domain.comment.entity.Comment;
import com.sparta.scheduledevelop.domain.comment.repository.CommentRepository;
import com.sparta.scheduledevelop.domain.schedule.entity.Schedule;
import com.sparta.scheduledevelop.domain.schedule.repository.ScheduleRepository;
import com.sparta.scheduledevelop.domain.user.entity.User;
import com.sparta.scheduledevelop.exception.CustomErrorCode;
import com.sparta.scheduledevelop.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    public CommentResponseDto createComment(User creator, Long scheduleId, CommentRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.SCHEDULE_NOT_FOUND));

        Comment comment = new Comment(requestDto.getText(), creator, schedule);
        return new CommentResponseDto(commentRepository.save(comment));
    }

    public List<CommentResponseDto> findComments(Long scheduleId) {
        return commentRepository.findAllByScheduleId(scheduleId).stream().map(CommentResponseDto::new).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(User user, Long scheduleId, Long commentId, CommentRequestDto requestDto) {
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.SCHEDULE_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.COMMENT_NOT_FOUND));
        if(!Objects.equals(user.getId(), comment.getCommentCreator().getId())) {
            throw new CustomException(CustomErrorCode.INVALID_UPDATE);
        }

        comment.update(requestDto.getText());
        return new CommentResponseDto(commentRepository.saveAndFlush(comment));
    }

    public void deleteComment(User user, Long scheduleId, Long commentId) {
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.SCHEDULE_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.COMMENT_NOT_FOUND));
        if(!Objects.equals(user.getId(), comment.getCommentCreator().getId())) {
            throw new CustomException(CustomErrorCode.INVALID_DELETE);
        }

        commentRepository.delete(comment);
    }
}
