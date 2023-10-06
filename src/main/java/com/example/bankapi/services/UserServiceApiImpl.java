package com.example.bankapi.services;

import com.example.bankapi.dto.*;
import com.example.bankapi.dto.responses.AccountsListResponse;
import com.example.bankapi.dto.responses.FalseResponse;
import com.example.bankapi.dto.responses.Response;
import com.example.bankapi.dto.responses.TrueResponse;
import com.example.bankapi.model.User;
import com.example.bankapi.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
@Log4j2
public class UserServiceApiImpl implements UserServiceApi {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceApiImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Response createUser(@NotNull UserDTO userDTO) {
        User user = userRepository.findByName(userDTO.getName()).orElse(null);
        Response response;
        if (user == null) {
            User newUser = new User();
            newUser.setName(userDTO.getName().trim());
            String pinCode = Base64.getEncoder().encodeToString(userDTO.getPinCode().getBytes(StandardCharsets.UTF_8));
            newUser.setPinCode(pinCode);
            newUser.setAccountNumber(createAccountNumber());
            userRepository.saveAndFlush(newUser);
            response = new TrueResponse(true, "Success, new User created");
        } else {
            response = new FalseResponse(false, "User with current name is already created. Please try another name");
        }
        return response;
    }

    @Override
    public Response addFunds(@NotNull AddFundsDTO addFundsDTO) {
        Response response;
        User user = userRepository.findByName(addFundsDTO.getName()).orElse(null);
        if (user != null){
            user.setBalance(user.getBalance() + addFundsDTO.getAmount());
            userRepository.saveAndFlush(user);
            response = new TrueResponse(true, "Funds added to uUser");
        } else {
            response = new FalseResponse(false, "Current user not found");
        }
        return response;
    }

    @Override
    public Response withDrawMoney(@NotNull WithdrawDTO withdrawDTO) {
        User user = userRepository.findByName(withdrawDTO.getName()).orElse(null);
        Response response;
        byte[] bytes = withdrawDTO.getPinCode().getBytes(StandardCharsets.UTF_8);
        if (user == null) {
            response = new FalseResponse(false, "Current user not found");
        } else if (pinCodeIsValid(user, bytes)) {
            response = new FalseResponse(false, "Wrong Pin Code");
        } else {
            response = getMoney(user, withdrawDTO);
        }
        return response;
    }

    @Override
    public Response transferMoney(@NotNull TransferDTO transferDTO) {
        System.out.println(transferDTO.getAmount());
        Response response;
        User userFrom = userRepository.findByAccountNumber(transferDTO.getAccountFrom()).orElse(null);
        User userTo = userRepository.findByAccountNumber(transferDTO.getAccountTo()).orElse(null);
        if (userFrom == null) {
            response = new FalseResponse(false, "User from which transfer should made, is not found.");
        } else if (userTo == null) {
            response = new FalseResponse(false, "The user to whom the transfer was supposed to be made was not found.");
        } else {
            byte[] bytes = transferDTO.getPinCode().getBytes(StandardCharsets.UTF_8);
            if (pinCodeIsValid(userFrom, bytes)) {
                response = new FalseResponse(false, "Wrong Pin Code");
            } else {
                response = transferMoneyBetweenAccount(userFrom, userTo, transferDTO.getAmount());
            }
        }
        return response;
    }

    @Override
    public Response getAllAccountsInfo() {
        Response response;
        List<User> userList = userRepository.findAll();
        if (!userList.isEmpty()){
            response = createAccountList(userList);
        } else {
            response = new FalseResponse(false, "Error, no available accounts");
        }
        return response;
    }

    private @NotNull Response createAccountList(@NotNull List<User> userList) {
        Response response;
        List<AccountDTO> accountsDTOList = userList.parallelStream()
                .map(user -> {
                    AccountDTO accountsDTO = new AccountDTO();
                    accountsDTO.setName(user.getName());
                    accountsDTO.setAccountNumber(user.getAccountNumber());
                    accountsDTO.setBalance(user.getBalance());
                    return accountsDTO;
                }).toList();
        response = new AccountsListResponse(true, accountsDTOList);
        return response;
    }

    private @NotNull Response transferMoneyBetweenAccount(@NotNull User userFrom, User userTo, long amount) {
        Response response;
        System.out.println(userFrom.getBalance() + " transfer");
        System.out.println(amount + " transfer");
        System.out.println(userFrom.getBalance() < amount);
        if (userFrom.getBalance() < amount) {
            response = new  FalseResponse(false, "Enough money on balance to transferring.");
        } else {
            userFrom.setBalance(userFrom.getBalance() - amount);
            userTo.setBalance(userTo.getBalance() + amount);
            userRepository.saveAndFlush(userFrom);
            userRepository.saveAndFlush(userTo);
            response = new TrueResponse(true, "Success, transfer completed");
        }
        return response;
    }

    private boolean pinCodeIsValid(@NotNull User user, byte[] bytes) {
        String checkablePin = Base64.getEncoder().encodeToString(bytes);
        return !user.getPinCode().equals(checkablePin);
    }

    private @NotNull Response getMoney(@NotNull User user, @NotNull WithdrawDTO withdrawDTO) {
        Response response;
        if (user.getBalance() >= withdrawDTO.getAmount()) {
            user.setBalance(user.getBalance() - withdrawDTO.getAmount());
            userRepository.saveAndFlush(user);
            response = new TrueResponse(true, "Success, money withdrawn");
        } else {
            response = new FalseResponse(false, "Enough money on balance");
        }
        return response;
    }

    private @NotNull String createAccountNumber() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(chars[rnd.nextInt(chars.length)]);
        }
        sb.append("-").append(100000 + rnd.nextInt(900000));
        userRepository.findByAccountNumber(sb.toString()).ifPresent(user -> createAccountNumber());
        return sb.toString();
    }
}