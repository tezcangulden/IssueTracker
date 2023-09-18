package com.snowfalltravel.issuetracker.controller;

import com.snowfalltravel.issuetracker.entity.*;
import com.snowfalltravel.issuetracker.service.BugService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@RestController
public class BugController {
    @Autowired
    private BugService bugService;

    @PostMapping("/bugs")
    public Bug createBug( @RequestBody Bug bug) {
        if (bug.getTitle() == null) {
            throw new IllegalArgumentException("Invalid bug definition!");
        }
        bug.setBugStatus(BugStatus.UNRESOLVED);
        return bugService.createBug(bug);
    }

    @RequestMapping(value = "/bugs", method = RequestMethod.PUT)
    public ResponseEntity<Bug> editBug(@RequestBody Bug bug, HttpServletResponse response) {
        if (bug == null) {
            throw new IllegalArgumentException("Missing bug details");
        }
        validateBugRequest(bug.getId());
        try {
            return  ResponseEntity.ok(bugService.editBug(bug));
        }catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage(),e);
        }
    }

    @DeleteMapping("bugs/{bugId}")
    public void deleteBug(@PathVariable Long bugId) {
        validateBugRequest(bugId);
        bugService.deleteBug(bugId);
    }

    private void validateBugRequest(Long bugId) {
        if (bugId == null) {
            throw new IllegalArgumentException("Missing bug details");
        }
    }

    @GetMapping("/bugs/")
    public List<BugDTO> viewBug(@RequestBody Bug bug) {

        return bugService.viewBug(bug) ;
    }

    @GetMapping("/bugs")
    public List<BugDTO> viewAllBugs() {
        return bugService.viewBugs();
    }

    @RequestMapping("/bugs/{bugId}/assignUser/{userId}")
    public Bug assignToUser(@PathVariable Long bugId , @PathVariable Long userId) {
        validateBugRequest(bugId);
        if (userId == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User Id");
        }
        return bugService.assignToUser(bugId, userId);

    }

    @PostMapping("/bugs/{bugId}/comments")
    public CommentDTO addComment(@PathVariable Long bugId, @RequestBody Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Missing comment details");
        }
        return bugService.addComment(bugId, comment);
    }

    @DeleteMapping("/bugs/{bugId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long bugId, @PathVariable Long commentId) {
        validateBugRequest(bugId);
        if (commentId == null) {
            throw new IllegalArgumentException("Missing comment details");
        }
        bugService.deleteComment(bugId, commentId);
    }
}
