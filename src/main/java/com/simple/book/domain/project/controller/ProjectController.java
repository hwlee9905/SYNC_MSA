package com.simple.book.domain.project.controller;

import com.simple.book.domain.project.dto.request.ProjectCreateRequestDto;
import com.simple.book.domain.project.dto.request.ProjectDeleteRequestDto;
import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user/project")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<String> projectCreate(@RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
        projectService.createProject(projectCreateRequestDto);
        return ResponseEntity.ok("OK");
    }
    @ResponseBody
    @PostMapping("/delete")
    public ResponseEntity<String> projectDelete(@RequestBody ProjectDeleteRequestDto projectDeleteRequestDto) {
        return ResponseEntity.ok(projectService.deleteProject(projectDeleteRequestDto));
    }
}
