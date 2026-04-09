package com.Nexora.NexoraFinance.transaction.services;


import com.Nexora.NexoraFinance.account.entity.Account;
import com.Nexora.NexoraFinance.account.repo.AccountRepo;
import com.Nexora.NexoraFinance.auth_users.entity.User;
import com.Nexora.NexoraFinance.auth_users.services.UserService;
import com.Nexora.NexoraFinance.enums.TransactionStatus;
import com.Nexora.NexoraFinance.enums.TransactionType;
import com.Nexora.NexoraFinance.exceptions.BadRequestException;
import com.Nexora.NexoraFinance.exceptions.InsufficientBalanceException;
import com.Nexora.NexoraFinance.exceptions.InvalidTransactionException;
import com.Nexora.NexoraFinance.exceptions.NotFoundException;
import com.Nexora.NexoraFinance.notification.dtos.NotificationDTO;
import com.Nexora.NexoraFinance.notification.services.NotificationService;
import com.Nexora.NexoraFinance.res.Response;
import com.Nexora.NexoraFinance.transaction.dtos.TransactionDTO;
import com.Nexora.NexoraFinance.transaction.dtos.TransactionRequest;
import com.Nexora.NexoraFinance.transaction.entity.Transaction;
import com.Nexora.NexoraFinance.transaction.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Transactional
    public Response<?> createTransaction(TransactionRequest transactionRequest) {

        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionRequest.getTransactionType());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());

        switch (transactionRequest.getTransactionType()) {
            case DEPOSIT -> handleDeposit(transactionRequest , transaction);
            case WITHDRAWAL -> handleWithdrawal(transactionRequest , transaction);
            case TRANSFER -> handleTransfer(transactionRequest , transaction);
            default -> throw  new InvalidTransactionException("Invalid transaction type");
        }

        transaction.setStatus(TransactionStatus.SUCCESS);
        Transaction savedTransaction = transactionRepo.save(transaction);

        //send a notification out

        sendTransactionNotification(savedTransaction);

        return Response.builder()
                .statusCode(200)
                .message("Transaction Successful")
                .build();


    }

    @Override
    public Response<List<TransactionDTO>> getTransactionsForMyAccount(String accountNumber, int page, int size) {
        User user = userService.getCurrentLoggedInUser();

        Account account = accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        //make sure the account belongs to the user , an extra security check

        if(!account.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Account does not belong to the authenticated user");
        }

        Pageable pageable = PageRequest.of(page, size , Sort.by("transactionDate").descending());
        Page<Transaction> txns = transactionRepo.findByAccount_AccountNumber(accountNumber, pageable);

        List<TransactionDTO> transactionDTOS = txns.getContent().stream()
                .map(transaction -> modelMapper.map(transaction , TransactionDTO.class))
                .toList();

        return Response.<List<TransactionDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Transactions retrieved")
                .meta(Map.of(
                        "currentPage" ,txns.getNumber(),
                        "totalItems", txns.getTotalElements(),
                        "totalPages" , txns.getTotalPages(),
                        "pageSize" , txns.getSize()

                ))
                .build();
    }

    private void handleDeposit(TransactionRequest request, Transaction transaction) {
        Account account = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        account.setBalance(account.getBalance().add(request.getAmount()));
        transaction.setAccount(account);
        accountRepo.save(account);

    }
    private void handleWithdrawal(TransactionRequest request, Transaction transaction) {
        Account account  = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        transaction.setAccount(account);
        accountRepo.save(account);

    }

    private void handleTransfer(TransactionRequest request, Transaction transaction) {
        Account sourceAccount = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        Account destinationAccount = accountRepo.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new NotFoundException("Desination Account not found"));

        if(sourceAccount.getBalance().compareTo(request.getAmount()) < 0){
            throw new InsufficientBalanceException("Insufficient balance in source account");
        }

        //Deduct from source account

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        accountRepo.save(sourceAccount);

        //Add to Destination
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
        accountRepo.save(destinationAccount);

        transaction.setAccount(sourceAccount);
        transaction.setSourceAccount(sourceAccount.getAccountNumber());
        transaction.setDestinationAccount(destinationAccount.getAccountNumber());
    }

    private void sendTransactionNotification(Transaction tnx) {
        User user = tnx.getAccount().getUser();
        String subject;
        String template;

        Map<String , Object> templateVariables = new HashMap<>();

        templateVariables.put("name" , user.getFirstName());
        templateVariables.put("amount" , tnx.getAmount());
        templateVariables.put("accountNumber" , tnx.getAccount().getAccountNumber());
        templateVariables.put("date" , tnx.getTransactionDate());
        templateVariables.put("balance" , tnx.getAccount().getBalance());

        if(tnx.getTransactionType() == TransactionType.DEPOSIT){
            subject = "Credit Alert";
            template = "credit-alert";

            NotificationDTO notificationEmailToSendOut = NotificationDTO.builder()
                    .recipient(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariables(templateVariables)
                    .build();

            notificationService.sendEmail(notificationEmailToSendOut , user);

        }else if(tnx.getTransactionType() == TransactionType.WITHDRAWAL){
            subject = "Debit Alert";
            template = "debit-alert";

            NotificationDTO notificationEmailToSendOut = NotificationDTO.builder()
                    .recipient(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariables(templateVariables)
                    .build();

            notificationService.sendEmail(notificationEmailToSendOut , user);

        } else if (tnx.getTransactionType()== TransactionType.TRANSFER) {
            subject = "Debit Alert";
            template = "debit-alert";

            NotificationDTO notificationEmailToSendOut = NotificationDTO.builder()
                    .recipient(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariables(templateVariables)
                    .build();

            notificationService.sendEmail(notificationEmailToSendOut , user);

            //Receiver CREDIT alert

            Account destinationAccount = accountRepo.findByAccountNumber(tnx.getDestinationAccount())
                    .orElseThrow(()-> new NotFoundException("Destination Account not found"));

            User receiver = destinationAccount.getUser();

            Map<String , Object> rcvVars = new HashMap<>();

            rcvVars.put("name" , receiver.getFirstName());
            rcvVars.put("amount" , tnx.getAmount());
            rcvVars.put("accountNumber" , destinationAccount.getAccountNumber());
            rcvVars.put("date" , tnx.getTransactionDate());
            rcvVars.put("balance" , destinationAccount.getBalance());
            rcvVars.put("senderAccount" , tnx.getSourceAccount());


            NotificationDTO notificationEmailToSendOutToReceiver = NotificationDTO.builder()
                    .recipient(receiver.getEmail())
                    .subject("Credit Alert")
                    .templateName("credit-alert")
                    .templateVariables(rcvVars)
                    .build();

            notificationService.sendEmail(notificationEmailToSendOutToReceiver , user);

        }

    }
}
