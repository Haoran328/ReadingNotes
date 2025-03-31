package cn.edu.xjtlu.readingnotes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.edu.xjtlu.readingnotes.model.ReadingLog;

@Repository
public interface ReadingLogRepo extends JpaRepository<ReadingLog, Integer> {
    public List<ReadingLog> findAllByUserId(int userId);
}
