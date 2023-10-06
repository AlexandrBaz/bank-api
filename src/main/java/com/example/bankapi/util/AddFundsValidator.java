package com.example.bankapi.util;

import com.example.bankapi.dto.AddFundsDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AddFundsValidator implements Validator {
    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return AddFundsDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        AddFundsDTO addFundsDTO = (AddFundsDTO) target;
        if(addFundsDTO.getAmount() <=0){
            errors.rejectValue("amount","", "Must be greater than zero");
        }
    }
}
