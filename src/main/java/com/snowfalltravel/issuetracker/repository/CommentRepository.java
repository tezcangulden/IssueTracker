package com.snowfalltravel.issuetracker.repository;

import com.snowfalltravel.issuetracker.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment,Long> {
    List<Comment> findByBugId(Long id);
}
