package com.profile.profilehama.controllers;
import com.profile.profilehama.entities.UserProfile;
import com.profile.profilehama.services.FileStorageService;
import com.profile.profilehama.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/user-profiles")
@CrossOrigin(origins = {"http://bedouinsstudios.com", "*"})

public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private FileStorageService fileStorageService;


    @PostMapping
    public UserProfile createUserProfile(@RequestParam("name") String name,
                                         @RequestParam("lastName") String lastName,
                                         @RequestParam("email") String email,
                                         @RequestParam("dateOfBirth") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                         @RequestParam("aboutMe") String aboutMe,
                                         @RequestParam("mobile") String mobile,
                                         @RequestParam("location") String location,
                                         @RequestParam("category") String category,
                                         @RequestParam("image") MultipartFile image) {
        try {
            String imagePath = fileStorageService.saveFile(image);

            UserProfile userProfile = new UserProfile();
            userProfile.setName(name);
            userProfile.setLastName(lastName);
            userProfile.setEmail(email);
            userProfile.setDateOfBirth(dateOfBirth);
            userProfile.setAboutMe(aboutMe);
            userProfile.setMobile(mobile);
            userProfile.setLocation(location);
            userProfile.setCategory(category); // Ajout de la catégorie
            userProfile.setProfileImagePath(imagePath);

            return userProfileService.createUserProfile(userProfile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUserProfile(@RequestParam("id") Long id, @RequestBody UserProfile updatedProfile) {
        UserProfile existingProfile = userProfileService.getUserProfileById(id);
        if (existingProfile == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Mise à jour des informations
        existingProfile.setName(updatedProfile.getName());
        existingProfile.setLastName(updatedProfile.getLastName());
        existingProfile.setEmail(updatedProfile.getEmail());
        existingProfile.setDateOfBirth(updatedProfile.getDateOfBirth());
        existingProfile.setAboutMe(updatedProfile.getAboutMe());
        existingProfile.setMobile(updatedProfile.getMobile());
        existingProfile.setLocation(updatedProfile.getLocation());
        existingProfile.setProfileImagePath(updatedProfile.getProfileImagePath());
        existingProfile.setCategory(updatedProfile.getCategory());

        userProfileService.updateUserProfile(existingProfile);
        return ResponseEntity.ok(Collections.singletonMap("message", "Profile updated successfully"));
    }
    @GetMapping
    public List<UserProfile> getAllUserProfiles() {
        return userProfileService.getAllUserProfiles();
    }

    @GetMapping("/{id}")
    public UserProfile getUserProfileById(@PathVariable Long id) {
        return userProfileService.getUserProfileById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserProfile(@PathVariable Long id) {
        userProfileService.deleteUserProfile(id);
    }
}