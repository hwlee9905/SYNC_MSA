package com.simple.book.domain.project.controller;

import com.simple.book.domain.project.dto.request.ProjectCreateRequestDto;
import com.simple.book.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user/project")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    @ResponseBody
    @PostMapping("/create")
    public String projectCreate(@RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
        log.warn(projectCreateRequestDto.getDescription(), projectCreateRequestDto.getTitle());
        return projectService.createProject(projectCreateRequestDto);
    }
}
