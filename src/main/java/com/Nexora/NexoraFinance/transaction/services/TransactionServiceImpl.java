package com.Nexora.NexoraFinance.transaction.services;


import com.Nexora.NexoraFinance.account.repo.AccountRepo;
import com.Nexora.NexoraFinance.auth_users.services.UserService;
import com.Nexora.NexoraFinance.notification.services.NotificationService;
import com.Nexora.NexoraFinance.res.Response;
import com.Nexora.NexoraFinance.transaction.dtos.TransactionDTO;
import com.Nexora.NexoraFinance.transaction.dtos.TransactionRequest;
import com.Nexora.NexoraFinance.transaction.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ModelMapper modelMapper;



    @Override
    public Response<?> createTransaction(TransactionRequest transactionRequest) {
        return null;
    }

    @Override
    public Response<List<TransactionDTO>> getTransactionsForAnAccount(String accountNumber, int page, int size) {
        return null;
    }
}
