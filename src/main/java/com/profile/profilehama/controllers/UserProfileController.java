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
@CrossOrigin(origins = {"https://bedouinsstudios.com", "*"})

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
    public ResponseEntity<?> updateUserProfile(
            @RequestParam("id") Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "dateOfBirth", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(value = "aboutMe", required = false) String aboutMe,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        // Récupération du profil existant
        UserProfile existingProfile = userProfileService.getUserProfileById(id);
        if (existingProfile == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        try {
            // Si une nouvelle image est fournie, la traiter et mettre à jour le chemin
            if (image != null && !image.isEmpty()) {
                String imagePath = fileStorageService.saveFile(image);
                existingProfile.setProfileImagePath(imagePath); // Mettre à jour le chemin de l'image
            }

            // Mise à jour des autres informations, en conservant la valeur précédente si nécessaire
            if (name != null) {
                existingProfile.setName(name);
            }
            if (lastName != null) {
                existingProfile.setLastName(lastName);
            }
            if (email != null) {
                existingProfile.setEmail(email);
            }
            if (dateOfBirth != null) {
                existingProfile.setDateOfBirth(dateOfBirth);
            }
            if (aboutMe != null) {
                existingProfile.setAboutMe(aboutMe);
            }
            if (mobile != null) {
                existingProfile.setMobile(mobile);
            }
            if (location != null) {
                existingProfile.setLocation(location);
            }

            // Mise à jour dans la base de données
            userProfileService.updateUserProfile(existingProfile);

            return ResponseEntity.ok(Collections.singletonMap("message", "Profile updated successfully"));

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to store file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
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