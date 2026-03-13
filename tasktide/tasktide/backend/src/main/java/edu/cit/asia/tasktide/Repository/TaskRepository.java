package edu.cit.asia.tasktide.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cit.asia.tasktide.Model.TaskModel;

public interface TaskRepository extends JpaRepository<TaskModel, Integer> {

    public List<TaskModel> findByUserUserId(int userId);

}