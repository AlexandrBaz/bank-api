package com.example.bankapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class WithdrawDTO {
    @NotEmpty(message = "Name is empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ._-]{2,}$", message = "The name must be more than 2 characters long.")
    private String name;

    @Min(value = 1, message = "Amount should be greater then zero")
    Long amount;

    @NotEmpty(message = "Must not empty")
    @NotNull
    @Pattern(regexp = "[0-9]{4}", message = "Error, PIN Code must be 4 digits.")
    private String pinCode;

}
