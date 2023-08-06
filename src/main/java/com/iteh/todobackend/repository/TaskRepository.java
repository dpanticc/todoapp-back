package com.iteh.todobackend.repository;

import com.iteh.todobackend.entity.Task;
import com.iteh.todobackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);

}
