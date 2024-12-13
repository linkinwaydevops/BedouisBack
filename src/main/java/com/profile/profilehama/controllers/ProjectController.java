package com.profile.profilehama.controllers;
import com.profile.profilehama.entities.Project;
import com.profile.profilehama.entities.UserProfile;
import com.profile.profilehama.services.FileStorageService;
import com.profile.profilehama.services.ProjectService;
import com.profile.profilehama.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/projects")
@CrossOrigin(origins = {"http://bedouinsstudios.com", "*"})
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping
    public Project createProject(@RequestParam("projectName") String projectName,
                                 @RequestParam("description") String description,
                                 @RequestParam("userProfileId") Long userProfileId,
                                 @RequestParam("images") List<MultipartFile> images) {
        try {
            List<String> imagePaths = new ArrayList<>();
            for (MultipartFile image : images) {
                // Enregistrer chaque image et ajouter le chemin à la liste
                imagePaths.add(fileStorageService.saveFile(image));
            }

            UserProfile userProfile = userProfileService.getUserProfileById(userProfileId);
            if (userProfile == null) {
                throw new IllegalArgumentException("User profile not found");
            }

            Project project = new Project();
            project.setProjectName(projectName);
            project.setDescription(description);
            project.setProjectImagePaths(imagePaths);
            project.setUserProfile(userProfile);

            return projectService.createProject(project);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable Long id,
                                 @RequestParam("projectName") String projectName,
                                 @RequestParam("description") String description,
                                 @RequestParam(value = "userProfileId", required = false) Long userProfileId,
                                 @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        try {
            Project project = projectService.getProjectById(id);

            project.setProjectName(projectName);
            project.setDescription(description);

            if (images != null) {
                List<String> imagePaths = new ArrayList<>();
                for (MultipartFile image : images) {
                    imagePaths.add(fileStorageService.saveFile(image));
                }
                project.setProjectImagePaths(imagePaths);
            }

            if (userProfileId != null) {
                UserProfile userProfile = userProfileService.getUserProfileById(userProfileId);
                project.setUserProfile(userProfile);
            }

            return projectService.updateProject(project);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }
    @GetMapping("/user/{userProfileId}")
    public List<Project> getProjectsByUserProfileId(@PathVariable Long userProfileId) {
        return projectService.getProjectsByUserProfileId(userProfileId);
    }
    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}
