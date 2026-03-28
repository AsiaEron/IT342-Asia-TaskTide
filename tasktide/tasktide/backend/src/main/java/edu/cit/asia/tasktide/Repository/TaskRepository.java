package edu.cit.asia.tasktide.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.cit.asia.tasktide.Model.TaskModel;

public interface TaskRepository extends JpaRepository<TaskModel, Integer> {

    @Query("SELECT t FROM TaskModel t WHERE t.user.user_id = :userId")
    List<TaskModel> findByUserId(@Param("userId") int userId);

}