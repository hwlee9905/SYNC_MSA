package com.simple.book.domain.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.project.dto.request.CreateProjectRequestDto;
import com.simple.book.domain.project.dto.request.DeleteProjectRequestDto;
import com.simple.book.domain.project.dto.request.GetProjectsRequestDto;
import com.simple.book.domain.project.service.ProjectService;
import com.simple.book.global.advice.ResponseMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/user/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    
    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createProject(@RequestBody CreateProjectRequestDto projectCreateRequestDto) {
        return ResponseEntity.ok().body(projectService.createProject(projectCreateRequestDto));
    }
    
    @PostMapping("/delete")
    public ResponseEntity<ResponseMessage> deleteProject(@RequestBody DeleteProjectRequestDto projectDeleteRequestDto) {
        return ResponseEntity.ok().body(projectService.deleteProject(projectDeleteRequestDto));
    }
    
    @PostMapping("/get")
    public ResponseEntity<ResponseMessage> getProjects(@RequestBody GetProjectsRequestDto getProjectsRequestDto) {
        return ResponseEntity.ok().body(projectService.getProjects(getProjectsRequestDto));
    }
}
