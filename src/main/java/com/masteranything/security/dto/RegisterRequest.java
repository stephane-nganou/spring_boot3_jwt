package com.masteranything.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @Size(min = 1, message = "First name is required and should be at least 1 character")
    String firstName,
    @Size(min = 1, message = "First name is required and should be at least 1 character")
    String lastName,
    @Email(message = "Email is required and should be valid")
    String email,
    @Size(min = 8, message = "Password is required and should be at least 8 characters")
    String password
) {

}
