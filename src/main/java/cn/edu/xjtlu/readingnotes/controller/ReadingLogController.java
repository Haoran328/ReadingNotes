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
    public List<ReadingLog> getReadingLogs() {
        return repo.findAll();
    }

    @GetMapping("/{id}/readinglog")
    public List<ReadingLog> getUserReadingLogs(@PathVariable int id) {
        return repo.findAllByUserId(id);
    }

    @PostMapping(
        path = "/{id}/readinglog",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<ReadingLog> getUserReadingLogsByFilters(
            @RequestParam Map<String,String> formData,
            @PathVariable int id) {
        Specification<ReadingLog> specs = ReadingLogSpecs.specify(id, formData);
        return repo.findAll(specs);
    }
    
    public void delete(int id) {
        repo.deleteById(id);
    }

    public void deleteByUserId(int userId) {
        repo.deleteByUserId(userId);
    }
}
