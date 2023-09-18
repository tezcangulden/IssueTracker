package com.snowfalltravel.issuetracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String body;
    @Column(name = "bug_status")
    @Enumerated(EnumType.STRING)
    private BugStatus bugStatus;
    @Transient
    @OneToMany(mappedBy = "bug", cascade = CascadeType.ALL)
    private List<Comment> commentList  ;



}
