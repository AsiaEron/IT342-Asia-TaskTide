package edu.cit.asia.tasktide.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.cit.asia.tasktide.shared.entity.TaskModel;
import edu.cit.asia.tasktide.shared.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public TaskModel addTask(TaskModel task) {
        return taskRepository.save(task);
    }

    public List<TaskModel> getUserTasks(int userId) {
        return taskRepository.findByUserId(userId);
    }

    public TaskModel editTask(int taskId, TaskModel updatedTask) {
        TaskModel existingTask = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        // Manually update only the editable fields to preserve the ID
        if (updatedTask.getTask_name() != null) {
            existingTask.setTask_name(updatedTask.getTask_name());
        }
        if (updatedTask.getDescription() != null) {
            existingTask.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getEnergy_level() != null) {
            existingTask.setEnergy_level(updatedTask.getEnergy_level());
        }
        if (updatedTask.getStatus() != null) {
            existingTask.setStatus(updatedTask.getStatus());
        }
        
        return taskRepository.save(existingTask);
    }

    public void deleteTask(int taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }
}
