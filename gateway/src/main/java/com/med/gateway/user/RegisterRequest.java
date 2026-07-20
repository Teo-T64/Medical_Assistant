package com.med.gateway.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email required")
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Password required")
    @Size(min = 8,message = "Must contain at least 8 characters")
    private String password;
    private String firstName;
    private String lastName;
    private String keycloakId;

}
