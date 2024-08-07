package project.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.service.ProjectService;
import project.service.dto.request.GetProjectsRequestDto;
import project.service.dto.response.GetProjectsResponseDto;
import project.service.global.ResponseMessage;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    final ProjectService projectService;
//    @PostMapping("/project/api/v1/find")
//    public ResponseMessage findProject(Long projectId)  {
//        return projectService.findProject(projectId);
//    }
//    @PostMapping("/project/api/v1/get")
//    public List<GetProjectsResponseDto> getProjects(@RequestBody GetProjectsRequestDto getProjectsRequestDto)  {
//        return projectService.getProjects(getProjectsRequestDto);
//    }
    @GetMapping("/project/api/v1")
    public ResponseMessage getProjects(@RequestParam List<Long> projectIds)  {
        return projectService.getProjects(projectIds);
    }
}
