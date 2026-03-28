package com.Nexora.NexoraFinance.account.services;

import com.Nexora.NexoraFinance.account.dtos.AccountDTO;
import com.Nexora.NexoraFinance.account.entity.Account;
import com.Nexora.NexoraFinance.account.repo.AccountRepo;
import com.Nexora.NexoraFinance.auth_users.entity.User;
import com.Nexora.NexoraFinance.auth_users.services.UserService;
import com.Nexora.NexoraFinance.enums.AccountStatus;
import com.Nexora.NexoraFinance.enums.AccountType;
import com.Nexora.NexoraFinance.enums.Currency;
import com.Nexora.NexoraFinance.exceptions.BadRequestException;
import com.Nexora.NexoraFinance.exceptions.NotFoundException;
import com.Nexora.NexoraFinance.res.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private final Random random = new Random();


    @Override
    public Account createAccount(AccountType accountType, User user) {

        String accountNumber = generateAccountNumber();
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(accountType)
                .currency(Currency.RUPEES)
                .balance(BigDecimal.ZERO)
                .accountStatus(AccountStatus.ACTIVE)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        return accountRepo.save(account);
    }

    @Override
    public Response<List<AccountDTO>> getMyAccounts() {
        User user = userService.getCurrentLoggedInUser();

        List<AccountDTO> accounts = accountRepo.findByUserId(user.getId())
                .stream()
                .map(account -> modelMapper.map(account , AccountDTO.class))
                .toList();

        return Response.<List<AccountDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User accounts fetched successfully")
                .data(accounts)
                .build();
    }

    @Override
    public Response<?> closeAccount(String accountNumber) {
        User user = userService.getCurrentLoggedInUser();
        Account account = accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new NotFoundException("AccountNotFound"));

        if(!user.getAccounts().contains(account)){
            throw new NotFoundException("Account does not belong to you");
        }
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0){
            throw new BadRequestException("Account balance must be zero before closing");
        }
        account.setAccountStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());
        accountRepo.save(account);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account closed successfully")
                .build();

    }

    private String generateAccountNumber() {
        String accountNumber;
        do{
            //Generate a random 8-digit number from (10,000,000 to 99,999,999)
            //and combine it with the "26" prefix.

            accountNumber = "26" + (random.nextInt(900000000) + 10000000);
        }while (accountRepo.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
    }
}
