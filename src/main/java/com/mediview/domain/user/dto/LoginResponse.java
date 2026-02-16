package com.mediview.domain.user.dto;

import com.mediview.domain.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;
    private String email;
    private UserRole role;
}
