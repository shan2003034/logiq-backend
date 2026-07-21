package com.logiq.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileResponse {
    private String firstName;
    private String lastName;
    private String companyName;
    private String email;
    private String profileImageUrl;
    private LocalDateTime createdAt;
}