package com.example.bankapi.util;

import com.example.bankapi.dto.WithdrawDTO;
import com.example.bankapi.services.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class WithdrawValidation implements Validator {
    private final UserService userService;
    private final ValidationPinCode validationPinCode;

    public WithdrawValidation(UserService userService, ValidationPinCode validationPinCode) {
        this.userService = userService;
        this.validationPinCode = validationPinCode;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return WithdrawDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        WithdrawDTO withdrawDTO = (WithdrawDTO) target;
        if (withdrawDTO.getAmount() == null) {
            errors.rejectValue("amount", "", "Amount not be null");
        } else if (withdrawDTO.getName() == null || withdrawDTO.getName().isEmpty()) {
            errors.rejectValue("amount", "", "Name not be Null");
        } else if (withdrawDTO.getPinCode() == null || withdrawDTO.getPinCode().isEmpty()) {
            errors.rejectValue("pinCode", "", "Pin Code is empty");
        } else if (userService.getAccountDto(withdrawDTO.getName()).getBalance() < withdrawDTO.getAmount()) {
            errors.rejectValue("amount", "", "Enough money on balance");
        } else if (withdrawDTO.getAmount() <= 0) {
            errors.rejectValue("amount", "", "Must be greater than zero");
        } else if (!validationPinCode.checkPinCode(withdrawDTO)) {
            errors.rejectValue("pinCode", "", "Pin Code not valid");
        }
    }
}
