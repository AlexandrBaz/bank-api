package com.example.bankapi.dto;

import lombok.Data;

@Data
public class AccountDTO {
    private String name;
    private String accountNumber;
    private long balance;
}
