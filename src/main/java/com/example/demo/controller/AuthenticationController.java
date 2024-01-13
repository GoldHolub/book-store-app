package com.example.demo.controller;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exception.RegistrationException;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User authentication", description = "Authentication of new user")
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
@Validated
@RestController
public class AuthenticationController {
    private final UserService userService;

    @Operation(summary = "User registration",
            description = "register a new user")
    @PostMapping(value = "/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
                                            throws RegistrationException {
        return userService.register(requestDto);
    }
}
