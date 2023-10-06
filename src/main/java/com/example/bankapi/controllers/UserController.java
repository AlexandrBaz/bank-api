package com.example.bankapi.controllers;

import com.example.bankapi.dto.*;
import com.example.bankapi.repositories.UserRepository;
import com.example.bankapi.services.UserService;
import com.example.bankapi.util.AddFundsValidator;
import com.example.bankapi.util.TransferValidation;
import com.example.bankapi.util.UserValidator;
import com.example.bankapi.util.WithdrawValidation;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/")
@Log4j2
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final AddFundsValidator addFundsValidator;
    private final WithdrawValidation withdrawValidation;
    private final TransferValidation transferValidation;


    @Autowired
    public UserController(UserService userService, UserRepository userRepository, UserValidator userValidator,
                          AddFundsValidator addFundsValidator,
                          WithdrawValidation withdrawValidation,
                          TransferValidation transferValidation) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.addFundsValidator = addFundsValidator;
        this.withdrawValidation = withdrawValidation;
        this.transferValidation = transferValidation;
    }

    @GetMapping("/")
    public String getIndex(@ModelAttribute("userDTO") UserDTO userDTO, @NotNull Model model) {
        List<AccountDTO> accountsDTOList = userService.getAllAccountsInfo();
        model.addAttribute("accountsDTOList", accountsDTOList);
        return "/index";
    }

    @PostMapping("/create")
    public String addUser(@ModelAttribute("userDTO") @Valid UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);
        if(bindingResult.hasErrors()){
            return "/index";
        }
        userService.createUser(userDTO);
        return "redirect:/";
    }

    @GetMapping("/user/{name}")
    public String userProfile(@PathVariable("name") String name, @ModelAttribute("addFundsDTO") AddFundsDTO addFundsDTO,
                              @NotNull Model model, @ModelAttribute("withdrawDTO") @NotNull WithdrawDTO withdrawDTO,
                              @ModelAttribute("transferDTO") @NotNull TransferDTO transferDTO) {
        AccountDTO accountDTO = userService.getAccountDto(name);
        model.addAttribute("accountDTO", accountDTO);
        model.addAttribute("accountDTOList", userService.getAllAccountsInfo());
        return "/user";
    }

    @PatchMapping("/add-funds-{name}")
    public String addFunds(@PathVariable("name") String name, @ModelAttribute("addFundsDTO") @Valid @NotNull AddFundsDTO addFundsDTO, BindingResult bindingResult) {
        addFundsDTO.setName(name);
        addFundsValidator.validate(addFundsDTO, bindingResult);
        if(bindingResult.hasErrors()){
            return "redirect:/user/{name}";
        }
        userService.addFunds(addFundsDTO);
        return "redirect:/user/{name}";
    }

    @PatchMapping("/withdraw-funds/{name}")
    public String withdrawFunds(@PathVariable("name") String name, @ModelAttribute("withdrawDTO") @Valid @NotNull WithdrawDTO withdrawDTO, BindingResult bindingResult) {
        withdrawDTO.setName(name);
        withdrawValidation.validate(withdrawDTO, bindingResult);
        if(bindingResult.hasErrors()){
            return "redirect:/user/{name}";
        }
        userService.withDrawMoney(withdrawDTO);
        return "redirect:/user/{name}";
    }

    @PatchMapping("/transfer-funds/{name}")
    public String transferFunds(@PathVariable("name") String name,
                                @ModelAttribute("transferDTO") @Valid @NotNull TransferDTO transferDTO,
                                BindingResult bindingResult) {
        transferDTO.setAccountFrom(Objects.requireNonNull(userRepository.findByName(name).orElse(null)).getAccountNumber());
        transferValidation.validate(transferDTO, bindingResult);
        if(bindingResult.hasErrors()){
            return "redirect:/user/{name}";
        }
        userService.transferMoney(transferDTO);
        return "redirect:/user/{name}";
    }
}