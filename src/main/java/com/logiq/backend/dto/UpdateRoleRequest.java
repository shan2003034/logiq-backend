package com.logiq.backend.dto;

import lombok.Data;

@Data
public class UpdateRoleRequest {
    private String role; // මෙතනට එන්නේ "DEVELOPER" හෝ "VIEWER"
}