package com.snowfalltravel.issuetracker.service;

import com.snowfalltravel.issuetracker.entity.*;
import com.snowfalltravel.issuetracker.repository.BugRepository;

import com.snowfalltravel.issuetracker.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;


@Service
public class BugService {
    @Autowired
    private BugRepository bugRepository;


    @Autowired
    private CommentRepository commentRepository;

    Logger logger = (Logger) LoggerFactory.getLogger(BugRepository.class);

    public Bug createBug(Bug bug) {
        try {
            return bugRepository.save(bug);
        }catch (Exception e) {
            logger.error("Bug record can not be created!", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Bug record can not be created!");
        }
    }

    public Bug editBug(Bug bug) {
        try{
            Bug updatedBug = bugRepository.findById(bug.getId()).orElseThrow();
            setUpdatedFields(bug, updatedBug);
            return bugRepository.save(updatedBug);
        }catch (NoSuchElementException e) {
            logger.error("Bug record can not be found!", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug record can not be found!");
        }catch(Exception e) {
            logger.error("Bug record can not be updated!", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Bug record can not be updated!");
        }
    }

    private void setUpdatedFields(Bug bug, Bug updatedBug) {
        if(bug.getBugStatus() != null) {
            updatedBug.setBugStatus(bug.getBugStatus());
        }
        if (bug.getTitle() != null) {
            updatedBug.setTitle(bug.getTitle());
        }
        if (bug.getBody() != null) {
            updatedBug.setBody(bug.getBody());
        }
    }

   public List<BugDTO> viewBugs() {
        try{
            List<Bug> bugList = (List<Bug>) bugRepository.findAll();
            return convertBugDTOList(bugList);
        }catch (Exception e) {
            logger.error("Bug records can not be searched!", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Bug records can not be searched!");
        }
    }

    public List<BugDTO> viewBug(Bug bug) {
        try {
            List<Bug> bugList;
            if (bug.getId() != null) {
                Bug bugByID = bugRepository.findById(bug.getId()).orElseThrow();
                bugList = Arrays.asList(bugByID);
            }else if (bug.getBugStatus() != null) {
                bugList = (List<Bug>) bugRepository.findAllByBugStatus(bug.getBugStatus());
            }else {
                bugList = (List<Bug>) bugRepository.findAll();
            }
            return convertBugDTOList(bugList);

        }catch (NoSuchElementException e) {
            logger.error("Bug record can not be found!", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug record can not be found!");
        }
    }

    public List<BugDTO>convertBugDTOList(List<Bug> bugList) {
        List<BugDTO> bugDTOList = new ArrayList<>();
        for (Bug bug: bugList
             ) {
            BugDTO bugDTO = BugDTO.convertDTO(bug);
            List<Comment> commentList = commentRepository.findByBugId(bug.getId());
            bugDTO.setCommentList(convertCommentDTOList(commentList));
            bugDTOList.add(bugDTO);

        }
        return bugDTOList;
    }

    public List<CommentDTO>convertCommentDTOList(List<Comment> commentList) {
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment: commentList
        ) {
            CommentDTO commentDTO = CommentDTO.convertDTO(comment);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }

    public void deleteBug(Long bugId) {
        try{
            bugRepository.deleteById(bugId);
        }catch (Exception e) {
            logger.error("Bug record can not be deleted!", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug record can not be deleted!");
        }

    }

    public Bug assignToUser(Long bugId, Long userId) {
         try{
            Bug bug = bugRepository.findById(bugId).orElseThrow();
            bug.setUserId(userId);
            return bugRepository.save(bug);
        }catch (NoSuchElementException e) {
            logger.error("Bug record can not be found!", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug record can not be found!");
        }catch (Exception e) {
            logger.error("Bug record can not be assigned to user!", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Bug record can not be assigned to user!");
        }
     }

    public CommentDTO addComment(Long bugId, Comment comment) {
        try{
            Bug bug = bugRepository.findById(bugId).orElseThrow();
            comment.setBug(bug);
            Comment savedComment = commentRepository.save(comment);
            return CommentDTO.convertDTO(savedComment);
        }catch (NoSuchElementException e) {
            logger.error("Bug record can not be found!", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug record can not be found!");
        }catch (Exception e) {
            logger.error("Comment can not be added!", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Comment can not be added!");
        }
    }

    public void deleteComment(Long bugId, Long commentId) {
        try{
            Bug bug= bugRepository.findById(bugId).orElseThrow();
            Comment comment = commentRepository.findById(commentId).orElseThrow();
            if (comment.getBug().getId() == bug.getId()) {
                commentRepository.deleteById(commentId);
            }else {
                 throw new ResponseStatusException(HttpStatus.CONFLICT, "Comment is not related to Bug!");
            }
        }catch (NoSuchElementException e) {
            logger.error("Bug record can not be found!", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug record can not be found!");
        }catch (Exception e) {
            logger.error("Comment can not be deleted!", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment can not be deleted!");
        }
    }
}
