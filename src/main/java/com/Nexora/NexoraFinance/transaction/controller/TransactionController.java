package com.Nexora.NexoraFinance.transaction.controller;

import com.Nexora.NexoraFinance.res.Response;
import com.Nexora.NexoraFinance.transaction.dtos.TransactionRequest;
import com.Nexora.NexoraFinance.transaction.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Response<?>> createTransaction(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Response<?>> getTransactionForMyAccount(@PathVariable String accountNumber
            , @RequestParam (defaultValue = "0") int page , @RequestParam (defaultValue = "50") int size) {

        return ResponseEntity.ok(transactionService.getTransactionsForMyAccount(accountNumber, page, size));

    }


}
