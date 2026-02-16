package com.mediview.domain.user.service;

import com.mediview.domain.exception.BadRequestException;
import com.mediview.domain.exception.NotFoundException;
import com.mediview.domain.user.dto.*;
import com.mediview.domain.user.entity.User;
import com.mediview.domain.user.entity.UserProfile;
import com.mediview.domain.user.repository.UserProfileRepository;
import com.mediview.domain.user.repository.UserRepository;
import com.mediview.global.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public LoginResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists: " + request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole())
                .build();
        userRepository.save(user);

        UserProfile profile = UserProfile.builder()
                .user(user)
                .name(request.getName())
                .build();
        userProfileRepository.save(profile);

        String token = jwtService.generateToken(user.getEmail());

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserProfileResponse getMyProfile(String email) {
        User user = findUserByEmail(email);
        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElse(null);

        return buildProfileResponse(user, profile);
    }

    @Override
    @Transactional
    public UserProfileResponse updateMyProfile(String email, UserProfileUpdateRequest request) {
        User user = findUserByEmail(email);
        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> UserProfile.builder().user(user).name("").build());

        if (request.getName() != null) profile.setName(request.getName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getBirthdate() != null) profile.setBirthdate(request.getBirthdate());
        if (request.getGender() != null) profile.setGender(request.getGender());

        userRepository.save(user);
        userProfileRepository.save(profile);

        return buildProfileResponse(user, profile);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));
    }

    private UserProfileResponse buildProfileResponse(User user, UserProfile profile) {
        UserProfileResponse.UserProfileResponseBuilder builder = UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .status(user.getStatus());

        if (profile != null) {
            builder.name(profile.getName())
                    .birthdate(profile.getBirthdate())
                    .gender(profile.getGender());
        }

        return builder.build();
    }
}
