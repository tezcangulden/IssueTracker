package com.snowfalltravel.issuetracker.repository;

import com.snowfalltravel.issuetracker.entity.Bug;
import com.snowfalltravel.issuetracker.entity.BugStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugRepository extends CrudRepository<Bug,Long> {

    List<Bug> findAllByBugStatus(BugStatus bugStatus);


}
