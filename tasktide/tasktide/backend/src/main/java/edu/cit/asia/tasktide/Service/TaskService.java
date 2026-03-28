package edu.cit.asia.tasktide.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.cit.asia.tasktide.Model.TaskModel;
import edu.cit.asia.tasktide.Repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    public TaskModel addTask(TaskModel task) {
        return taskRepository.save(task);
    }

    public List<TaskModel> getUserTasks(int userId){
        return taskRepository.findByUserId(userId);
    }

    public void deleteTask(int taskId) {
        taskRepository.deleteById(taskId);
    }

}