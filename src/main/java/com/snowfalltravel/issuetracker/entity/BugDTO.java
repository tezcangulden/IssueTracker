package com.snowfalltravel.issuetracker.entity;


import lombok.Builder;
import lombok.Data;


import java.util.List;

@Builder
@Data
public class BugDTO {
    private Long id;
    private String title;
    private String body;
    private BugStatus bugStatus;
    private List<CommentDTO> commentList  ;
    private Long userId;
    public static BugDTO convertDTO(Bug bug) {
        BugDTO bugDTO = BugDTO.builder().bugStatus(bug.getBugStatus())
                .body(bug.getBody())
                .id(bug.getId())
                .userId(bug.getUserId())
                .title(bug.getTitle()).build();

        return bugDTO;
    }
}
