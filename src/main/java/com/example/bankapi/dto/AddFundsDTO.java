package com.example.bankapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddFundsDTO {
    @NotEmpty(message = "Name is empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ._-]{2,}$", message = "The name must be more than 2 characters long.")
    private String name;

    @Min(value = 1, message = "Amount should be greater then zero")
    private long amount;

}
