package cn.edu.xjtlu.readingnotes.readinglog.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import cn.edu.xjtlu.readingnotes.readinglog.ReadingLog;
import cn.edu.xjtlu.readingnotes.readinglog.ReadingLogRepo;
import cn.edu.xjtlu.readingnotes.util.UserInfo;

@RestController
@RequestMapping("/api/log")
public class ReadingLogRestController {

    @Autowired
    private ReadingLogRepo repo;

    @PostMapping(params = "action=create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createLog(
            @AuthenticationPrincipal Object principal,
            @RequestParam Map<String,String> params) throws Exception {
        if (principal instanceof UserInfo) {
            final Long userId = ((UserInfo) principal).getId();
            ReadingLog log = new ReadingLog(
                params.get("title"),
                params.get("author"),
                LocalDate.parse(params.get("date")),
                Integer.parseInt(params.get("spentTime")),
                params.get("note"),
                userId);
            repo.save(log);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/log"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }
        throw new Exception();
    }
    
    @PutMapping("/{logId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> updateLog(
            @AuthenticationPrincipal Object principal,
            @PathVariable Long logId,
            @RequestParam Map<String,String> params)
            throws Exception {
        if ((principal instanceof UserInfo)) {
            final Long userId = ((UserInfo) principal).getId();
            ReadingLog log = new ReadingLog(
                params.get("title"),
                params.get("author"),
                LocalDate.parse(params.get("date")),
                Integer.parseInt(params.get("spentTime")),
                params.get("note"),
                userId);
            log.setId(logId);
            repo.save(log);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/log"));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        throw new Exception();
    }
    
    @DeleteMapping("/{logId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteLog(@PathVariable Long logId)
            throws Exception {
        repo.deleteById(logId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/log"));
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }
}
