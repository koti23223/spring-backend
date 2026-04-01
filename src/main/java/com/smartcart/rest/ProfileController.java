package com.smartcart.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.smartcart.entity.User;
import com.smartcart.repository.UserRepository;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    private final String uploadDir = "uploads";

    @GetMapping("/{email}")
    public ResponseEntity<?> getProfile(@PathVariable String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        return ResponseEntity.ok(optionalUser.get());
    }

    @PutMapping(value = "/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(
            @PathVariable String email,
            @RequestParam("fullName") String fullName,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "bio", required = false) String bio,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        User existingUser = optionalUser.get();

        existingUser.setFullName(fullName);
        existingUser.setAddress(address);
        existingUser.setBio(bio);

        if (file != null && !file.isEmpty()) {
            try {
                Files.createDirectories(Paths.get(uploadDir));

                String originalFileName = file.getOriginalFilename();
                String safeFileName = System.currentTimeMillis() + "_" + originalFileName;
                Path filePath = Paths.get(uploadDir, safeFileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                existingUser.setProfileImage("http://localhost:8080/uploads/" + safeFileName);

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to upload image");
            }
        }

        userRepository.save(existingUser);

        return ResponseEntity.ok(existingUser);
    }
}