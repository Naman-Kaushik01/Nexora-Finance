package com.Nexora.NexoraFinance.account.services;

import com.Nexora.NexoraFinance.account.dtos.AccountDTO;
import com.Nexora.NexoraFinance.account.entity.Account;
import com.Nexora.NexoraFinance.account.repo.AccountRepo;
import com.Nexora.NexoraFinance.auth_users.entity.User;
import com.Nexora.NexoraFinance.auth_users.services.UserService;
import com.Nexora.NexoraFinance.enums.AccountType;
import com.Nexora.NexoraFinance.res.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return null;
    }

    @Override
    public Response<List<AccountDTO>> getMyAccounts() {
        return null;
    }

    @Override
    public Response<?> closeAccount(String accountNumber) {
        return null;
    }
}
