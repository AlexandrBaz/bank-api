package com.example.bankapi.services;

import com.example.bankapi.dto.*;
import com.example.bankapi.dto.responses.Response;
import org.springframework.stereotype.Service;

@Service
public interface UserServiceApi {
    Response createUser(UserDTO userDTO);

    Response addFunds(AddFundsDTO addFundsDTO);

    Response withDrawMoney(WithdrawDTO withdrawDTO);

    Response transferMoney(TransferDTO transferDTO);

    Response getAllAccountsInfo();
}
