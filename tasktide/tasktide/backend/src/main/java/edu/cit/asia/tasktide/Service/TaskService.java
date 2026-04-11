package edu.cit.asia.tasktide.Service;

import java.util.List;

import org.springframework.beans.BeanUtils;
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

    public TaskModel editTask(int taskId, TaskModel updatedTask) {
        TaskModel existingTask = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        // Copies all matching fields except IDs
        BeanUtils.copyProperties(updatedTask, existingTask, "taskId", "id");
        return taskRepository.save(existingTask);
    }

    public void deleteTask(int taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }
}