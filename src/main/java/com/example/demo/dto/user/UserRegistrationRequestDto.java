package com.example.demo.dto.user;

import com.example.demo.annotation.FieldMatch;
import jakarta.validation.constraints.Email;
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
    @NotNull
    @Email
    private String email;
    @NotBlank
    private String password;
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
