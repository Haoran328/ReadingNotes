package cn.edu.xjtlu.readingnotes.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.xjtlu.readingnotes.model.ReadingLog;
import cn.edu.xjtlu.readingnotes.repository.ReadingLogRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



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

    @PostMapping(path = "/{id}/readinglog", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<ReadingLog> getUserReadingLogsByFilters(
            @RequestParam Map<String,String> formData,
            @PathVariable int id
    ) {
        List<ReadingLog> result = repo.findAllByUserId(id);
        if (formData.containsKey("title")) {
            filterByString(result, formData.get("title"));
        }
        if (formData.containsKey("author")) {
            filterByString(result, formData.get("author"));
        }
        if (formData.containsKey("start_date")) {
            filterByDate(result, LocalDate.parse(formData.get("start_date")), -1);
        }
        if (formData.containsKey("end_date")) {
            filterByDate(result, LocalDate.parse(formData.get("end_date")), 1);
        }
        if (formData.containsKey("min_time")) {
            filterByNumber(result, Integer.parseInt(formData.get("min_time")), -1);
        }
        if (formData.containsKey("max_time")) {
            filterByNumber(result, Integer.parseInt(formData.get("max_time")), 1);
        }
        return result;
    }

    private void filterByString(List<ReadingLog> source, String target) {
        //TODO: Implement filtering according to string comparison
    }

    private void filterByDate(List<ReadingLog> source, LocalDate bound, int sign) {
        ListIterator<ReadingLog> iterator = source.listIterator();
        while (iterator.hasNext()) {
            if (bound.compareTo(iterator.next().getDate()) * sign < 0) {
                iterator.remove();
            }
        }
    }

    private void filterByNumber(List<ReadingLog> source, Number bound, int sign) {
        //TODO: Implement filtering according to number comparison
    }
    
}
