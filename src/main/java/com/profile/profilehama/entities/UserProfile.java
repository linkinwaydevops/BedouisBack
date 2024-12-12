package com.profile.profilehama.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate; // Import nécessaire pour LocalDate
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;  // Nouveau champ
    private String email;
    private LocalDate dateOfBirth; // Nouveau champ
    private String aboutMe;   // Nouveau champ
    private String mobile;    // Nouveau champ
    private String location;  // Nouveau champ
    private String category;  // Nouveau champ pour la catégorie

    // Stocker le chemin de l'image
    private String profileImagePath;
    @JsonIgnore
    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    // Les getters et setters sont générés par Lombok
}
