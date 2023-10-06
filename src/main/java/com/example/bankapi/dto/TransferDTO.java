package com.example.bankapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDTO {
    @Pattern(regexp = "^[a-zA-Z0-9 ._-]{2,}$", message = "The name must be more than 2 characters long.")
    private String accountFrom;
    @Min(value = 1, message = "Amount should be greater then zero")
    private Long amount;

    @NotEmpty
    @Pattern(regexp = "[0-9]{4}", message = "Error, PIN Code must be 4 digits.")
    private String pinCode;

    @NotEmpty(message = "Name is empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ._-]{2,}$", message = "The name must be more than 2 characters long.")
    private String accountTo;
}
