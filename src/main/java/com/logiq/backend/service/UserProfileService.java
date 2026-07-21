package com.logiq.backend.service;

import com.logiq.backend.dto.ChangePasswordRequest;
import com.logiq.backend.dto.UpdateProfileRequest;
import com.logiq.backend.dto.UserProfileResponse;
import com.logiq.backend.model.User;
import com.logiq.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${file.profile-image-dir}")
    private String uploadDir;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 1. Profile Data ලබා ගැනීම
    public UserProfileResponse getUserProfile(String email) {
        User user = getUserByEmail(email);
        return UserProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .companyName(user.getCompanyName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .createdAt(user.getCreatedAt())
                .build();
    }

    // 2. Profile Data වෙනස් කිරීම
    public void updateProfile(String email, UpdateProfileRequest request) {
        User user = getUserByEmail(email);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCompanyName(request.getCompanyName());
        userRepository.save(user);
    }

    // 3. Password එක වෙනස් කිරීම
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = getUserByEmail(email);

        // පරණ Password එක හරිදැයි බැලීම
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // අලුත් Password එක Encode කරලා Save කිරීම
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // 4. Profile Image එක Upload කිරීම
    public String uploadProfileImage(String email, MultipartFile file) throws IOException {
        User user = getUserByEmail(email);

        // ෆෝල්ඩර් එක නැත්නම් අලුතින් හදනවා
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // ආරක්ෂිතව File Name එකක් හැදීම (උදා: 123e4567-e89b..._image.png)
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

        // ෆයිල් එක Save කිරීම
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Database එකේ Save කරන්න URL එක හදාගන්නවා
        String imageUrl = "/profile-images/" + fileName;
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }
}