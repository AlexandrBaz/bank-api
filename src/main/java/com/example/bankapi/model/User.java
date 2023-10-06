package com.example.bankapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name="user_account")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;

    @NotEmpty(message = "Name is empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ._-]{3,}$", message = "The name must be more than 2 characters long.")
    @Column(nullable = false, unique = true)
    private String name;

    @NotEmpty
    @Column(nullable = false)
    private String pinCode;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Min(0)
    private long balance;
}
