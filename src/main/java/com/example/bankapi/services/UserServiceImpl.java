package com.example.bankapi.services;

import com.example.bankapi.dto.*;
import com.example.bankapi.model.User;
import com.example.bankapi.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private static final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(@NotNull UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        String pinCode = Base64.getEncoder().encodeToString(userDTO.getPinCode().getBytes(StandardCharsets.UTF_8));
        user.setPinCode(pinCode);
        user.setAccountNumber(createAccountNumber());
        userRepository.saveAndFlush(user);
    }

    @Override
    public AccountDTO getAccountDto(String name) {
        User user = userRepository.findByName(name).orElse(null);
        AccountDTO accountDTO = new AccountDTO();
        if(user != null) {
            accountDTO.setName(user.getName());
            accountDTO.setAccountNumber(user.getAccountNumber());
            accountDTO.setBalance(user.getBalance());
        }
        return accountDTO;
    }

    @Override
    public void addFunds(@NotNull AddFundsDTO addFundsDTO) {
        userRepository.findByName(addFundsDTO.getName()).ifPresent(user -> {
            user.setBalance(user.getBalance() + addFundsDTO.getAmount());
            userRepository.saveAndFlush(user);
        });
    }

    @Override
    public void withDrawMoney(@NotNull WithdrawDTO withdrawDTO) {
        userRepository.findByName(withdrawDTO.getName()).ifPresent(user -> {
            user.setBalance(user.getBalance() - withdrawDTO.getAmount());
            userRepository.saveAndFlush(user);
        });
    }

    @Override
    public void transferMoney(@NotNull TransferDTO transferDTO) {
        User userFrom = userRepository.findByAccountNumber(transferDTO.getAccountFrom()).orElse(null);
        User userTo = userRepository.findByAccountNumber(transferDTO.getAccountTo()).orElse(null);
        if(userFrom != null && userTo !=null) {
            userFrom.setBalance(userFrom.getBalance() - transferDTO.getAmount());
            userTo.setBalance(userTo.getBalance() + transferDTO.getAmount());
            userRepository.saveAndFlush(userFrom);
            userRepository.saveAndFlush(userTo);
        }
    }

    @Override
    public List<AccountDTO> getAllAccountsInfo() {
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()){
            return null;
        }
        return userList.parallelStream()
                .map(user -> {
                    AccountDTO accountsDTO = new AccountDTO();
                    accountsDTO.setName(user.getName());
                    accountsDTO.setAccountNumber(user.getAccountNumber());
                    accountsDTO.setBalance(user.getBalance());
                    return accountsDTO;
                }).toList();
    }

    private @NotNull String createAccountNumber() {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(CHARS[rnd.nextInt(CHARS.length)]);
        }
        sb.append("-").append(100000 + rnd.nextInt(900000));
        userRepository.findByAccountNumber(sb.toString()).ifPresent(user -> createAccountNumber());
        return sb.toString();
    }

}
