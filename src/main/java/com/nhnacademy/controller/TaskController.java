package com.nhnacademy.controller;

import com.nhnacademy.model.task.dto.*;
import com.nhnacademy.model.task.entity.Task;
import com.nhnacademy.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("project/{projectId}/task/{taskId}")
    public ResponseEntity<ResponseTaskDto> getTask(@PathVariable long projectId, @PathVariable long taskId) {
        Task task = taskService.getTaskById(taskId);
        ResponseTaskDto responseTaskDto = new ResponseTaskDto(task.getTaskId(), task.getTaskTitle(), task.getTaskDescription(), task.getMilestone().getMilestoneId());
        return ResponseEntity.status(HttpStatus.OK).body(responseTaskDto);
    }

    @GetMapping("project/{projectId}/task")
    public ResponseEntity<ResponseTaskListDto> getTasks(@PathVariable long projectId) {
        List<Task> taskList = taskService.getAllTasksByProjectId(projectId);
        ResponseTaskListDto responseTaskListDto = new ResponseTaskListDto();
        for(Task task : taskList) {
            responseTaskListDto.getTasks().add(
                    new ResponseTaskDto(task.getTaskId(),
                            task.getTaskTitle(),
                            task.getTaskDescription(),
                            task.getMilestone().getMilestoneId()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseTaskListDto);
    }

    @PostMapping("/project/{projectId}/task")
    public ResponseEntity<ResponseTaskIdDto> registerTask(@RequestBody TaskRegisterRequest taskRegisterRequest, @PathVariable Long projectId) {
        Task task = taskService.save(taskRegisterRequest, projectId);
        ResponseTaskIdDto responseTaskIdDto = new ResponseTaskIdDto(task.getTaskId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseTaskIdDto);
    }

    @PutMapping("/project/{projectId}/task/{taskId}")
    public ResponseEntity<String> updateTask(@RequestBody TaskUpdateRequest taskUpdateRequest, @PathVariable long projectId, @PathVariable long taskId) {
        taskService.updateTask(taskId, taskUpdateRequest);
        return ResponseEntity.ok("Task updated");
    }

    @DeleteMapping("/project/{projectId}/task/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable long projectId, @PathVariable long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("task deleted");
    }

}
