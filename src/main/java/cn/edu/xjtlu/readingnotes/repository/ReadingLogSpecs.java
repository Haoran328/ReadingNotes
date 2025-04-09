package cn.edu.xjtlu.readingnotes.repository;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import cn.edu.xjtlu.readingnotes.model.ReadingLog;
import cn.edu.xjtlu.readingnotes.model.ReadingLog_;

public class ReadingLogSpecs {
    public static Specification<ReadingLog> specify(Long userId, Map<String,String> formData) {
        Specification<ReadingLog> specs = createdBy(userId);
        if (formData.containsKey("title")) {
            specs = specs.and(titledWith(formData.get("title")));
        }
        if (formData.containsKey("author")) {
            specs = specs.and(authoredBy(formData.get("author")));
        }
        String startDate = formData.get("start_date");
        String endDate = formData.get("end_date");
        if (startDate != null && endDate != null) {
            specs = specs.and(between(LocalDate.parse(startDate), LocalDate.parse(endDate)));
        } else if (startDate != null) {
            specs = specs.and(since(LocalDate.parse(startDate)));
        } else if (endDate != null) {
            specs = specs.and(until(LocalDate.parse(endDate)));
        }
        String minTime = formData.get("min_time");
        String maxTime = formData.get("max_time");
        if (minTime != null && maxTime != null) {
            specs = specs.and(between(Integer.parseInt(minTime), Integer.parseInt(maxTime)));
        } else if (minTime != null) {
            specs = specs.and(between(Integer.parseInt(minTime), Integer.MAX_VALUE));
        } else if (maxTime != null) {
            specs = specs.and(between(0, Integer.parseInt(maxTime)));
        }
        return specs;
    }

    public static Specification<ReadingLog> createdBy(Long userId) {
        return (root, query, builder) -> {
            return builder.equal(root.get(ReadingLog_.userId), userId);
        };
    }

    public static Specification<ReadingLog> since(LocalDate date) {
        return (root, query, builder) -> {
            return builder.greaterThanOrEqualTo(root.get(ReadingLog_.date), date);
        };
    }

    public static Specification<ReadingLog> until(LocalDate date) {
        return (root, query, builder) -> {
            return builder.lessThanOrEqualTo(root.get(ReadingLog_.date), date);
        };
    }

    public static Specification<ReadingLog> between(LocalDate lower, LocalDate upper) {
        return (root, query, builder) -> {
            return builder.between(root.get(ReadingLog_.date), lower, upper);
        };
    }

    public static Specification<ReadingLog> between(Integer lower, Integer upper) {
        return (root, query, builder) -> {
            return builder.between(root.get(ReadingLog_.spentTime), lower, upper);
        };
    }

    public static Specification<ReadingLog> authoredBy(String author) {
        return (root, query, builder) -> {
            return builder.like(root.get(ReadingLog_.author), author);
        };
    }

    public static Specification<ReadingLog> titledWith(String title) {
        return (root, query, builder) -> {
            return builder.like(root.get(ReadingLog_.title), title);
        };
    }
}
