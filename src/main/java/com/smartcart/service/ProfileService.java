package com.smartcart.service;

import java.io.IOException;
import java.nio.file.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smartcart.dto.ProfileRequest;
import com.smartcart.dto.ProfileResponse;
import com.smartcart.entity.User;
import com.smartcart.repository.UserRepository;

@Service
public class ProfileService {

    private final UserRepository userRepository;

    // 🔥 Upload folder
    private final String uploadDir = "uploads/";

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ GET PROFILE
    public ProfileResponse getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    // ✅ UPDATE PROFILE (WITH IMAGE)
    public ProfileResponse updateProfile(
            String email,
            ProfileRequest request,
            MultipartFile file) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 UPDATE TEXT FIELDS (NULL SAFE)
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

//        if (request.getPhone() != null) {
//            user.setPhone(request.getPhone());
//        }

        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        // 🔥 HANDLE IMAGE UPLOAD
        if (file != null && !file.isEmpty()) {
            try {

                // create folder if not exists
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // unique file name
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                Path filePath = uploadPath.resolve(fileName);

                // save file
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // save URL in DB
                user.setProfileImage("http://localhost:8080/uploads/" + fileName);

            } catch (IOException e) {
                throw new RuntimeException("Image upload failed");
            }
        }

        userRepository.save(user);

        ProfileResponse response = mapToResponse(user);
        response.setMessage("Profile updated successfully");

        return response;
    }

    // ✅ COMMON MAPPING
    private ProfileResponse mapToResponse(User user) {

        ProfileResponse response = new ProfileResponse();

        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
//        response.setPhone(user.getPhone());
        response.setAddress(user.getAddress());
        response.setBio(user.getBio());
        response.setProfileImage(user.getProfileImage());

        return response;
    }
}