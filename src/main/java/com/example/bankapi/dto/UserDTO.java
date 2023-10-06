package com.example.bankapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDTO {
    @NotEmpty(message = "Name is empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ._-]{3,}$", message = "The name must be more than 3 characters long.")
    private String name;

    @NotEmpty
    @Pattern(regexp = "[0-9]{4}", message = "Error, PIN code must be 4 digits.")
    private String pinCode;
}
