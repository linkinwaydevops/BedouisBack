package com.profile.profilehama.services;
import com.profile.profilehama.entities.UserProfile;
import com.profile.profilehama.repositories.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile createUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public UserProfile updateUserProfile(UserProfile userProfile) {
        return userProfileRepository.findById(userProfile.getId()).map(existingProfile -> {
            existingProfile.setName(userProfile.getName());
            existingProfile.setLastName(userProfile.getLastName());
            existingProfile.setEmail(userProfile.getEmail());
            existingProfile.setDateOfBirth(userProfile.getDateOfBirth());
            existingProfile.setAboutMe(userProfile.getAboutMe());
            existingProfile.setMobile(userProfile.getMobile());
            existingProfile.setLocation(userProfile.getLocation());
            existingProfile.setProfileImagePath(userProfile.getProfileImagePath());
            existingProfile.setCategory(userProfile.getCategory());
            return userProfileRepository.save(existingProfile);
        }).orElseThrow(() -> new IllegalArgumentException("User profile not found"));
    }
    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    public UserProfile getUserProfileById(Long id) {
        return userProfileRepository.findById(id).orElse(null);
    }



    public void deleteUserProfile(Long id) {
        userProfileRepository.deleteById(id);
    }
}