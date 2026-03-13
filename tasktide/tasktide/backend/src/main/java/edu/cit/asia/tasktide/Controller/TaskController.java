package edu.cit.asia.tasktide.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.asia.tasktide.Model.TaskModel;
import edu.cit.asia.tasktide.Service.TaskService;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("/add")
    public TaskModel addTask(@RequestBody TaskModel task) {
        return taskService.addTask(task);
    }

    @GetMapping("/user/{userId}")
    public List<TaskModel> getUserTasks(@PathVariable int userId) {
        return taskService.getUserTasks(userId);
    }

    @DeleteMapping("/delete/{taskId}")
    public void deleteTask(@PathVariable int taskId) {
        taskService.deleteTask(taskId);
    }

}