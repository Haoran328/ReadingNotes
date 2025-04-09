package cn.edu.xjtlu.readingnotes.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;

import cn.edu.xjtlu.readingnotes.model.ReadingLog;
import cn.edu.xjtlu.readingnotes.repository.ReadingLogRepo;
import cn.edu.xjtlu.readingnotes.repository.ReadingLogSpecs;



@RestController
public class ReadingLogController {
    @Autowired
    private ReadingLogRepo repo;

    @GetMapping("/review")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReadingLog> getAllLogs() {
        return repo.findAll();
    }

    @PostMapping("/{userId}/log")
    @PreAuthorize("#userId == authentication.principal.id")
    public ReadingLog createLog(@PathVariable Long userId, @RequestBody ReadingLog log) {
        log.setUserId(userId);
        return repo.save(log);
    }

    @GetMapping("/{userId}/log")
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')")
    public List<ReadingLog> getUserReadingLogs(@PathVariable Long userId) {
        return repo.findAllByUserId(userId);
    }

    @PostMapping(
        path = "/{userId}/log",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<ReadingLog> getUserReadingLogsByFilters(
            @RequestParam Map<String,String> formData,
            @PathVariable Long userId) {
        Specification<ReadingLog> specs = ReadingLogSpecs.specify(userId, formData);
        return repo.findAll(specs);
    }
    
    @DeleteMapping("/{logId}")
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')")
    public void deleteLog(
        @PathVariable Long userId,
        @PathVariable Long logId) {
        repo.deleteById(logId);
    }
    
    @PutMapping("/{logId}")
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')")
    public ReadingLog updateLog(
        @PathVariable Long userId,
        @PathVariable Long logId,
        @RequestBody ReadingLog log) {
        log.setUserId(userId);
        return repo.save(log);
    }
}
