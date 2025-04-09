package cn.edu.xjtlu.readingnotes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.edu.xjtlu.readingnotes.model.User;
import cn.edu.xjtlu.readingnotes.util.Role;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAllByRole(Role role);
}