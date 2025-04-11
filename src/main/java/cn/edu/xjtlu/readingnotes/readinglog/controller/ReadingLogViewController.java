package cn.edu.xjtlu.readingnotes.readinglog.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import cn.edu.xjtlu.readingnotes.readinglog.ReadingLog;
import cn.edu.xjtlu.readingnotes.readinglog.ReadingLogService;
import cn.edu.xjtlu.readingnotes.util.UserInfo;
import org.springframework.web.bind.annotation.RequestParam;





@Controller
public class ReadingLogViewController {

    @Autowired
    private ReadingLogService readingLogService;

    private Supplier<? extends Exception> supplier = () -> new Exception("Log not found");

    @GetMapping("/log")
    @PreAuthorize("authenticated")
    public String viewLog(Authentication authentication, Model model) throws Exception {
        List<ReadingLog> logs;
        if (authentication.getAuthorities().stream().findAny().orElseThrow().getAuthority().endsWith("ADMIN")) {
            logs = readingLogService.getLogs(null);
        } else {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserInfo) {
                logs = readingLogService.getLogs(((UserInfo) principal).getId());
            } else {
                throw new Exception();
            }
        }
        model.addAttribute("logs", logs);
        return "logs";
    }
    
    @GetMapping(path = "/log", params = "action=create")
    @PreAuthorize("hasRole('USER')")
    public String createLog() {
        return "log-edit";
    }

    @GetMapping(path = "/log", params = "action=filter")
    @PreAuthorize("hasRole('USER')")
    public String showFilters(
            @AuthenticationPrincipal Object principal)
            throws Exception {
        if (!(principal instanceof UserInfo)) {
            throw new Exception();
        }
        return "filter";
    }

    @PostMapping(path = "/log", params = "action=filter", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PreAuthorize("hasRole('USER')")
    public String getUserReadingLogsByFilters(
            @AuthenticationPrincipal Object principal,
            @RequestParam Map<String,String> formData,
            Model model)
            throws Exception {
        if (!(principal instanceof UserInfo)) {
            throw new Exception();
        }
        final Long userId = ((UserInfo) principal).getId();
        model.addAttribute("logs", readingLogService.filterLogs(userId, formData));
        return "filter";
    }

    @GetMapping(path = "/log/{logId}", params = "action=edit")
    @PreAuthorize("hasRole('USER')")
    public String editLog(@PathVariable Long logId, Model model) throws Exception {
        ReadingLog log = readingLogService.getLog(logId).orElseThrow(supplier);
        model.addAttribute("log", log);
        return "log-edit";
    }
    
    @GetMapping("/log/{logId}")
    @PreAuthorize("authenticated")
    public String viewLog(
            Authentication authentication,
            @PathVariable Long logId,
            Model model)
            throws Exception {
        ReadingLog log = readingLogService.getLog(logId).orElseThrow(supplier);
        if (authentication.getAuthorities().stream().findAny().orElseThrow().getAuthority().endsWith("ADMIN")) {
            model.addAttribute("log", log);
            return "log";
        } else {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserInfo) {
                model.addAttribute("log", log);
                return "log";
            } else {
                throw new Exception("Unauthorized");
            }
        }
    }
    
}
