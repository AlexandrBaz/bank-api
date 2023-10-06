package com.example.bankapi.util;

import com.example.bankapi.dto.UserDTO;
import com.example.bankapi.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(@NotNull Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userRepository.existsByName(userDTO.getName())){
            errors.rejectValue("name","", "User with current Name already exists. Try another name!");
        }
        if (userDTO.getPinCode().length() != 4){
            errors.rejectValue("pinCode","", "The PIN code must consist of 4 digits.");
        }
        if (userDTO.getPinCode().matches("[0-9]")){
            errors.rejectValue("pinCode", "","Only digits");
        }
    }
}