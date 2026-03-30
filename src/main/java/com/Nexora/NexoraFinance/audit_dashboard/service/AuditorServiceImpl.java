package com.Nexora.NexoraFinance.audit_dashboard.service;

import com.Nexora.NexoraFinance.account.dtos.AccountDTO;
import com.Nexora.NexoraFinance.account.repo.AccountRepo;
import com.Nexora.NexoraFinance.auth_users.dtos.UserDTO;
import com.Nexora.NexoraFinance.auth_users.repo.UserRepo;
import com.Nexora.NexoraFinance.transaction.dtos.TransactionDTO;
import com.Nexora.NexoraFinance.transaction.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditorServiceImpl implements AuditorService {

    private final UserRepo userRepo;
    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;
    private final ModelMapper modelMapper;

    @Override
    public Map<String, Long> getSystemTotals() {
        return Map.of();
    }

    @Override
    public Optional<UserDTO> findUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<AccountDTO> findAccountDetailsByAccountNumber(String accountNumber) {
        return Optional.empty();
    }

    @Override
    public List<TransactionDTO> findTransactionsByAccountNumber(String accountNumber) {
        return List.of();
    }

    @Override
    public Optional<TransactionDTO> findTransactionById(Long transactionId) {
        return Optional.empty();
    }
}
