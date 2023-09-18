package com.snowfalltravel.issuetracker.repository;

import com.snowfalltravel.issuetracker.entity.BugAssignments;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugAssignmentRepository extends CrudRepository<BugAssignments,Long> {
    @Query(value = "Select userId from bugassignments where bug_id=?1",nativeQuery = true)
    Long findUserIdByBugId(Long id);
}
