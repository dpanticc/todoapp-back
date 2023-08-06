package com.iteh.todobackend.controller;

import com.iteh.todobackend.dto.TaskDTO;
import com.iteh.todobackend.entity.Task;
import com.iteh.todobackend.entity.User;
import com.iteh.todobackend.service.TaskService;
import com.iteh.todobackend.service.UserService;
import com.iteh.todobackend.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final JwtService jwtService;


    @GetMapping
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return tasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody TaskDTO taskDTO) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Use the username to get the user from the database
        User user = userService.loadUserByUsername(username);

        // Associate the task with the user
        Task newTask = convertToEntity(taskDTO);
        newTask.setUser(user);

        Task savedTask = taskService.addTask(newTask);
        return new ResponseEntity<>(convertToDto(savedTask), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            return new ResponseEntity<>(convertToDto(task), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO updatedTaskDTO) {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            // Get the currently authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Use the username to get the user from the database
            User currentUser = userService.loadUserByUsername(username);

            // Check if the currently logged-in user owns the task
            if (task.getUser().equals(currentUser)) {
                // User is authorized to update the task
                Task updatedTask = convertToEntity(updatedTaskDTO);
                updatedTask.setId(id);
                // Exclude the 'user' property from being updated
                updatedTask.setUser(task.getUser());
                Task updatedTaskResponse = taskService.updateTask(updatedTask);
                return new ResponseEntity<>(convertToDto(updatedTaskResponse), HttpStatus.OK);
            } else {
                // User is not authorized to update the task
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            // Get the currently authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Use the username to get the user from the database
            User currentUser = userService.loadUserByUsername(username);

            // Check if the currently logged-in user owns the task
            if (task.getUser().equals(currentUser)) {
                // User is authorized to delete the task
                taskService.deleteTask(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                // User is not authorized to delete the task
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/user/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksForCurrentUser() {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Use the username to get the user from the database
        User currentUser = userService.loadUserByUsername(username);

        // Get tasks associated with the user
        List<Task> tasks = taskService.getTasksForUser(currentUser);

        // Convert tasks to DTOs and return the response
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(taskDTOs, HttpStatus.OK);
    }

    private TaskDTO convertToDto(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setName(task.getName());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setCompleted(task.isCompleted());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setUserId(task.getUser() != null ? task.getUser().getId() : null);
        return taskDTO;
    }

    private Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setCompleted(taskDTO.isCompleted());
        task.setDueDate(taskDTO.getDueDate());
        task.setPriority(taskDTO.getPriority());

        return task;
    }
}
