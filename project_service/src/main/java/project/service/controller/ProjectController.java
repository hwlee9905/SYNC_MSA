package project.service.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.service.ProjectService;
import project.service.global.SuccessResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    
    @GetMapping("node2/project/api/v1")
    public SuccessResponse getProjects(HttpServletRequest request, @RequestParam(name="projectIds") List<Long> projectIds)  {
    	return projectService.getProjects(request, projectIds);
    }
    @GetMapping("node2/project/thumbnail/{thumbnail}")
    public ResponseEntity<Resource> getProjectThumbnail(@PathVariable("thumbnail") String thumbnail) {
    	return projectService.getProjectThumbnail(thumbnail);
    }

}
