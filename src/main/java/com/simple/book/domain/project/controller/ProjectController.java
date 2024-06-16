package com.simple.book.domain.project.controller;

import com.simple.book.domain.project.dto.request.CreateProjectRequestDto;
import com.simple.book.domain.project.dto.request.DeleteProjectRequestDto;
import com.simple.book.domain.project.dto.request.GetProjectsRequestDto;
import com.simple.book.domain.project.dto.response.GetProjectsResponseDto;
import com.simple.book.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user/project")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    @PostMapping("/create")
    public ResponseEntity<String> createProject(@RequestBody CreateProjectRequestDto projectCreateRequestDto) {
        projectService.createProject(projectCreateRequestDto);
        return ResponseEntity.ok("OK");
    }
    @PostMapping("/delete")
    public ResponseEntity<String> deleteProject(@RequestBody DeleteProjectRequestDto projectDeleteRequestDto) {
        return ResponseEntity.ok(projectService.deleteProject(projectDeleteRequestDto));
    }
    @PostMapping("/get")
    public ResponseEntity<List<GetProjectsResponseDto>> getProjects(@RequestBody GetProjectsRequestDto getProjectsRequestDto) {
        return ResponseEntity.ok(projectService.getProjects(getProjectsRequestDto));
    }
}
