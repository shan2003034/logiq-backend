package com.logiq.backend.controller;

import com.logiq.backend.dto.ChangePasswordRequest;
import com.logiq.backend.dto.UpdateProfileRequest;
import com.logiq.backend.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/users/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    // Get Profile Details
    @GetMapping
    public ResponseEntity<?> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userProfileService.getUserProfile(authentication.getName()));
    }

    // 2. සාමාන්‍ය විස්තර යාවත්කාලීන කිරීම
    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request,
                                           Authentication authentication) {
        userProfileService.updateProfile(authentication.getName(), request);
        return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
    }

    // 3. Password වෙනස් කිරීම
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request,
                                            Authentication authentication) {
        try {
            userProfileService.changePassword(authentication.getName(), request);
            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 4. රූපය Upload කිරීම
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         Authentication authentication) {
        try {
            String imageUrl = userProfileService.uploadProfileImage(authentication.getName(), file);
            return ResponseEntity.ok(Map.of(
                    "message", "Profile image updated successfully",
                    "imageUrl", imageUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to upload image"));
        }
    }
}