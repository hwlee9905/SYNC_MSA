package project.service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.service.ProjectService;
import project.service.global.SuccessResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    final ProjectService projectService;
    @GetMapping("node2/project/api/v1")
    public SuccessResponse getProjects(@RequestParam(name="projectIds") List<Long> projectIds)  {
        return projectService.getProjects(projectIds);
    }
}
