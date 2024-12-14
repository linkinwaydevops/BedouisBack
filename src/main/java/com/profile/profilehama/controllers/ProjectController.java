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
@CrossOrigin(origins = {"https://bedouinsstudios.com", "*"})
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
            List<String> mediaPaths = new ArrayList<>();
            for (MultipartFile media : images) {
                // Vérifier le type de fichier
                String fileType = media.getContentType();
                if (!fileType.startsWith("image/") && !fileType.startsWith("video/")) {
                    throw new IllegalArgumentException("Type de fichier non pris en charge: " + media.getOriginalFilename());
                }

                // Enregistrer chaque image/vidéo et ajouter le chemin à la liste
                mediaPaths.add(fileStorageService.saveFile(media));
            }

            UserProfile userProfile = userProfileService.getUserProfileById(userProfileId);
            if (userProfile == null) {
                throw new IllegalArgumentException("User profile not found");
            }

            Project project = new Project();
            project.setProjectName(projectName);
            project.setDescription(description);
            project.setProjectImagePaths(mediaPaths); // Renommez la méthode si nécessaire
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
                List<String> mediaPaths = new ArrayList<>();
                for (MultipartFile media : images) {
                    // Vérifier le type de fichier
                    String fileType = media.getContentType();
                    if (!fileType.startsWith("image/") && !fileType.startsWith("video/")) {
                        throw new IllegalArgumentException("Type de fichier non pris en charge: " + media.getOriginalFilename());
                    }

                    mediaPaths.add(fileStorageService.saveFile(media));
                }
                project.setProjectImagePaths(mediaPaths); // Renommez en conséquence
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
