package com.logiq.backend.dto;

import com.logiq.backend.enums.ProjectRole;
import lombok.Data;

@Data
public class AddCollaboratorDto {
    private String email;
    private ProjectRole role;
}