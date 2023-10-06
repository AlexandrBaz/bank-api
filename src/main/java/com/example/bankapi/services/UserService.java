package com.example.bankapi.services;

import com.example.bankapi.dto.*;

import java.util.List;

public interface UserService {

    void createUser(UserDTO userDTO);

    AccountDTO getAccountDto(String name);

    void addFunds(AddFundsDTO addFundsDTO);

    void withDrawMoney(WithdrawDTO withdrawDTO);

    void transferMoney(TransferDTO transferDTO);

    List<AccountDTO> getAllAccountsInfo();
}
