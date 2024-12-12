package com.profile.profilehama.services;

import com.profile.profilehama.entities.Project;
import com.profile.profilehama.entities.UserProfile;
import com.profile.profilehama.repositories.ProjectRepository;
import com.profile.profilehama.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public Project createProject(Project project) {
        if (project.getUserProfile() != null && project.getUserProfile().getId() != null) {
            UserProfile userProfile = userProfileRepository.findById(project.getUserProfile().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User profile not found"));
            project.setUserProfile(userProfile);
        }
        return projectRepository.save(project);
    }
    public List<Project> getProjectsByUserProfileId(Long userProfileId) {
        return projectRepository.findByUserProfileId(userProfileId);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }



    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }


    public Project updateProject(Project project) {
        // Vérifiez l'existence du projet avant de le mettre à jour
        if (projectRepository.existsById(project.getId())) {
            return projectRepository.save(project);
        } else {
            throw new IllegalArgumentException("Project not found");
        }
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found");
        }
        projectRepository.deleteById(id);
    }
}
