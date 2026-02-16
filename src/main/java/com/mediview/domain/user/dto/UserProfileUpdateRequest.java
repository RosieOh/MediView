package com.mediview.domain.user.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileUpdateRequest {

    private String name;
    private String phone;
    private LocalDate birthdate;
    private String gender;
}
