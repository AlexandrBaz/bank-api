package com.example.bankapi.util;

import com.example.bankapi.dto.TransferDTO;
import com.example.bankapi.dto.WithdrawDTO;
import com.example.bankapi.model.User;
import com.example.bankapi.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Component
public class ValidationPinCode {
    public final UserRepository userRepository;

    public ValidationPinCode(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkPinCode(@NotNull TransferDTO transferDTO){
        byte[] bytes = transferDTO.getPinCode().getBytes(StandardCharsets.UTF_8);
        User user = userRepository.findByAccountNumber(transferDTO.getAccountFrom()).orElse(null);
        return pinCodeIsValid(Objects.requireNonNull(user), bytes);
    }

    public boolean checkPinCode(@NotNull WithdrawDTO WithdrawDTO){
        byte[] bytes = WithdrawDTO.getPinCode().getBytes(StandardCharsets.UTF_8);
        User user = userRepository.findByName(WithdrawDTO.getName()).orElse(null);
        return pinCodeIsValid(Objects.requireNonNull(user), bytes);
    }

    private boolean pinCodeIsValid(@NotNull User user, byte[] bytes){
        String checkablePin = Base64.getEncoder().encodeToString(bytes);
        return user.getPinCode().equals(checkablePin);
    }
}
