package com.profile.profilehama.repositories;

import com.profile.profilehama.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUserProfileId(Long userProfileId);

}