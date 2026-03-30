package com.Nexora.NexoraFinance.audit_dashboard.service;

import com.Nexora.NexoraFinance.account.dtos.AccountDTO;
import com.Nexora.NexoraFinance.auth_users.dtos.UserDTO;
import com.Nexora.NexoraFinance.transaction.dtos.TransactionDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AuditorService {

    Map<String , Long> getSystemTotals();
    Optional<UserDTO> findUserByEmail(String email);
    Optional<AccountDTO> findAccountDetailsByAccountNumber(String accountNumber);
    List<TransactionDTO> findTransactionsByAccountNumber(String accountNumber);
    Optional<TransactionDTO> findTransactionById(Long transactionId);
}
