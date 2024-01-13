package com.example.demo.dto.user;

import com.example.demo.annotation.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "repeatPassword",
                    message = "The password fields must match")
})
@Data
@Validated
public class UserRegistrationRequestDto {
    @NotBlank
    @NotNull
    private String email;
    @NotBlank
    @NotNull
    private String password;
    @NotBlank
    @NotNull
    private String repeatPassword;
    @NotBlank
    @NotNull
    private String firstName;
    @NotBlank
    @NotNull
    private String lastName;
    private String shippingAddress;
}
