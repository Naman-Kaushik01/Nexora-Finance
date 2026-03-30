package com.Nexora.NexoraFinance.audit_dashboard.controller;

import com.Nexora.NexoraFinance.account.dtos.AccountDTO;
import com.Nexora.NexoraFinance.audit_dashboard.service.AuditorService;
import com.Nexora.NexoraFinance.auth_users.dtos.UserDTO;
import com.Nexora.NexoraFinance.transaction.dtos.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUDITOR')")
public class AuditorController {

    private final AuditorService auditorService;

    @GetMapping("/totals")
    public ResponseEntity<Map<String, Long>> getSystemtotals() {
        return ResponseEntity.ok(auditorService.getSystemTotals());
    }
    @GetMapping("/users")
    public ResponseEntity<UserDTO> findUsersByEmail(@RequestParam String email) {
        Optional<UserDTO> userDTO = auditorService.findUserByEmail(email);

        return userDTO.map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @GetMapping("/account")
    public ResponseEntity<AccountDTO> findAccountDetailsByAccountNumber(@RequestParam String accountNumber) {
        Optional<AccountDTO> accountDTO = auditorService.findAccountDetailsByAccountNumber(accountNumber);

        return accountDTO.map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }
    @GetMapping("/transactions/by-accounts")
    public ResponseEntity<List<TransactionDTO>>getTransactionsByAccountNumber(@RequestParam String accountNumber) {
        List<TransactionDTO> transactionDTOList = auditorService.findTransactionsByAccountNumber(accountNumber);

       if (transactionDTOList.isEmpty()) {
           return ResponseEntity.noContent().build();
       }else {
           return ResponseEntity.ok(transactionDTOList);
       }
    }

    @GetMapping("/transactions/by-id")
    public ResponseEntity<TransactionDTO> getTransactionsById(@RequestParam Long id) {
        Optional<TransactionDTO> transactionDTO = auditorService.findTransactionById(id);

        return transactionDTO.map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

}
