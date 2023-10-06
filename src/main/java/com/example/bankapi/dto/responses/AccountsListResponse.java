package com.example.bankapi.dto.responses;

import com.example.bankapi.dto.AccountDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Value
@Data
public class AccountsListResponse extends Response {
    boolean success;
    List<AccountDTO> accountsDTOList;
}
