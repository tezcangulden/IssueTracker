package com.snowfalltravel.issuetracker.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentDTO {
    private Long id;
    private String title;
    private String body;
    public static CommentDTO convertDTO(Comment comment) {
        CommentDTO commentDTO = CommentDTO.builder().body(comment.getBody())
                .title(comment.getTitle())
                .id(comment.getId()).build();
        return commentDTO;
    }
}
