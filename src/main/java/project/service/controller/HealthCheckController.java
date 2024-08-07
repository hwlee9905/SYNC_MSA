package project.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project/api/v1")
@RequiredArgsConstructor
@Slf4j
public class HealthCheckController {
    @GetMapping("/test")
    public ResponseEntity<String> responseTest() {
        return ResponseEntity.ok("health checking..");
    }
}