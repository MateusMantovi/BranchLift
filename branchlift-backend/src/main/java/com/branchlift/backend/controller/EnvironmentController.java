package com.branchlift.backend.controller;

import com.branchlift.backend.model.Environment;
import com.branchlift.backend.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/environments")
public class EnvironmentController {

    @Autowired
    private EnvironmentService environmentService;

    @GetMapping
    public ResponseEntity<List<Environment>> getAllEnvironments() {
        List<Environment> environments = environmentService.getAllEnvironments();
        return new ResponseEntity<>(environments, HttpStatus.OK);
    }

    @PostMapping("/request-provisioning")
    public ResponseEntity<Environment> requestProvisioning(
            @RequestParam Long projectId,
            @RequestParam String gitBranch,
            @RequestParam String createdBy) {

        try {
            Environment savedEnvironment = environmentService.provisionNewEnvironment(projectId, gitBranch, createdBy);
            return new ResponseEntity<>(savedEnvironment, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // O método delete continua o mesmo, se você o tiver
}