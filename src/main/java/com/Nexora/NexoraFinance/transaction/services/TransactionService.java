package com.Nexora.NexoraFinance.transaction.services;

import com.Nexora.NexoraFinance.res.Response;
import com.Nexora.NexoraFinance.transaction.dtos.TransactionDTO;
import com.Nexora.NexoraFinance.transaction.dtos.TransactionRequest;

import java.util.List;


public interface TransactionService {
    Response<?>createTransaction(TransactionRequest transactionRequest);
    Response<List<TransactionDTO>> getTransactionsForAnAccount(String accountNumber , int page , int size);

}
