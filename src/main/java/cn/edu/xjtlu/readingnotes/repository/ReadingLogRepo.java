package cn.edu.xjtlu.readingnotes.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.edu.xjtlu.readingnotes.model.ReadingLog;

@Repository
public interface ReadingLogRepo
        extends CrudRepository<ReadingLog, Integer>,
                JpaSpecificationExecutor<ReadingLog> {
    public List<ReadingLog> findAllByUserId(int userId);
    public List<ReadingLog> findAll();
    public List<ReadingLog> findAll(Specification<ReadingLog> spec);
    public void deleteByUserId(int userId);
}
