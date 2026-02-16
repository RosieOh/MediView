package com.mediview.domain.user.dto;

import com.mediview.domain.enums.UserRole;
import com.mediview.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserProfileResponse {

    private Long id;
    private String email;
    private String phone;
    private UserRole role;
    private UserStatus status;
    private String name;
    private LocalDate birthdate;
    private String gender;
}
