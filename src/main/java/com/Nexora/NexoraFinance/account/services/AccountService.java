package com.Nexora.NexoraFinance.account.services;

import com.Nexora.NexoraFinance.account.dtos.AccountDTO;
import com.Nexora.NexoraFinance.account.entity.Account;
import com.Nexora.NexoraFinance.auth_users.entity.User;
import com.Nexora.NexoraFinance.enums.AccountType;
import com.Nexora.NexoraFinance.res.Response;

import java.util.List;

public interface AccountService {
    Account createAccount(AccountType accountType , User user);
    Response<List<AccountDTO>> getMyAccounts();
    Response<?>closeAccount(String accountNumber);
}
