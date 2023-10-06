package com.example.bankapi.util;

import com.example.bankapi.dto.TransferDTO;
import com.example.bankapi.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class TransferValidation implements Validator {
    private final UserRepository userRepository;
    private final ValidationPinCode validationPinCode;

    @Autowired
    public TransferValidation(UserRepository userRepository, ValidationPinCode validationPinCode) {
        this.userRepository = userRepository;
        this.validationPinCode = validationPinCode;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return TransferDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        TransferDTO transferDTO = (TransferDTO) target;
        long balance = Objects.requireNonNull(userRepository.findByAccountNumber(transferDTO.getAccountFrom()).orElse(null)).getBalance();
        if (transferDTO.getAmount() == null) {
            errors.rejectValue("amount", "", "Amount null");
        } else if (transferDTO.getPinCode() == null || transferDTO.getPinCode().isBlank()) {
            errors.rejectValue("pinCode", "", "Pin Code is empty");
        } else if (transferDTO.getAccountFrom() == null || transferDTO.getAccountFrom().isBlank()) {
            errors.rejectValue("accountTo", "", "Account from dont be empty");
        } else if (transferDTO.getAccountTo() == null || transferDTO.getAccountTo().isEmpty()) {
            errors.rejectValue("accountTo", "", "Account to dont be empty");
        } else if (!validationPinCode.checkPinCode(transferDTO)) {
            errors.rejectValue("pinCode", "", "Pin Code not valid");
        } else if (transferDTO.getAmount() <= 0) {
            errors.rejectValue("amount", "", "Must be greater than zero");
        } else if (balance < transferDTO.getAmount()) {
            errors.rejectValue("amount", "", "Enough money on balance");
        } else if (transferDTO.getAccountFrom().equals(transferDTO.getAccountTo())) {
            errors.rejectValue("accountTo", "", "Account TO == Account From");
        }
    }
}
