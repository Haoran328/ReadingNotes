package cn.edu.xjtlu.readingnotes.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/{id}/log")
    @PreAuthorize("#id == authentication.principal.id")
    public ReadingLog createLog(@PathVariable Long id, @RequestBody ReadingLog log) {
        log.setUserId(id);
        return repo.save(log);
    }

    // 在getUserReadingLogs方法添加权限控制
    @GetMapping("/{userId}/readinglog")
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')")
    public List<ReadingLog> getUserReadingLogs(@PathVariable Long userId) {  // int → Long
        return repo.findAllByUserId(userId);
    }

    @PostMapping(
        path = "/{userId}/log",  // 路径修正为/log
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<ReadingLog> getUserReadingLogsByFilters(
            @RequestParam Map<String,String> formData,
            @PathVariable Long userId) {  // 类型修正为Long
        Specification<ReadingLog> specs = ReadingLogSpecs.specify(id, formData);
        return repo.findAll(specs);
    }
    
    // 修改deleteLog方法参数类型
    @DeleteMapping("/{logId}")
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')")
    public void deleteLog(
        @PathVariable Long userId,  // 修改为Long类型
        @PathVariable int logId) {
        repo.deleteById(logId);
    }
    
    // 修改updateLog方法参数类型
    @PutMapping("/{logId}")
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')")
    public ReadingLog updateLog(
        @PathVariable Long userId,  // 保持Long类型
        @PathVariable int logId,
        @RequestBody ReadingLog log) {
        log.setUserId(userId);
        return repo.save(log);
    }
}
