package com.logiq.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberResponse {
    private Long userId;
    private String name;
    private String email;
    private String role; // "OWNER"
}