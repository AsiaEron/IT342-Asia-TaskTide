package edu.cit.asia.tasktide.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.cit.asia.tasktide.Model.TaskModel;
import edu.cit.asia.tasktide.Service.TaskService;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskModel> addTask(@RequestBody TaskModel task) {
        return ResponseEntity.ok(taskService.addTask(task));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskModel>> getUserTasks(@PathVariable int userId) {
        return ResponseEntity.ok(taskService.getUserTasks(userId));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskModel> editTask(@PathVariable int taskId, @RequestBody TaskModel task) {
        return ResponseEntity.ok(taskService.editTask(taskId, task));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable int taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully.");
    }
}