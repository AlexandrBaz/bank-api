package com.example.bankapi.controllers;

import com.example.bankapi.dto.AddFundsDTO;
import com.example.bankapi.dto.TransferDTO;
import com.example.bankapi.dto.UserDTO;
import com.example.bankapi.dto.WithdrawDTO;
import com.example.bankapi.dto.responses.*;
import com.example.bankapi.services.UserServiceApi;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final UserServiceApi userServiceApi;
    private Response response;

    @Autowired
    public ApiController(UserServiceApi userServiceApi) {
        this.userServiceApi = userServiceApi;
    }

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Response> addUser(@Valid @RequestBody @NotNull UserDTO userDTO) {
        response = userServiceApi.createUser(userDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/addFunds", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Response> addFundsToUser(@Valid @RequestBody @NotNull AddFundsDTO addFundsDTO){
        response = userServiceApi.addFunds(addFundsDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/withdraw",consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Response> withDrawMoney(@Valid @RequestBody WithdrawDTO withdrawDTO) {
        response = userServiceApi.withDrawMoney(withdrawDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/transfers", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Response> transferMoneyBetweenAccounts(@Valid @RequestBody TransferDTO transferDTO) {
        response = userServiceApi.transferMoney(transferDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/get-all")
    ResponseEntity<Response> getAllAccountsInfo() {
        response = userServiceApi.getAllAccountsInfo();
        return ResponseEntity.ok(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(@NotNull MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
