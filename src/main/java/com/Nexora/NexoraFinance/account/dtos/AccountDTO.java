package com.Nexora.NexoraFinance.account.dtos;

import com.Nexora.NexoraFinance.auth_users.dtos.UserDTO;
import com.Nexora.NexoraFinance.auth_users.entity.User;
import com.Nexora.NexoraFinance.enums.AccountStatus;
import com.Nexora.NexoraFinance.enums.AccountType;
import com.Nexora.NexoraFinance.enums.Currency;
import com.Nexora.NexoraFinance.transaction.dtos.TransactionDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private Long id;

    private String accountNumber;

    private BigDecimal balance;

    private AccountType accountType;

    @JsonBackReference // this will not be added to the account dto. It will be ignored because it is a back reference.
    private UserDTO user;

    private Currency currency;

    private AccountStatus accountStatus;

    @JsonManagedReference // it helps avoid recursion log by ignoring the userDTO within the AccountDTO
    private List<TransactionDTO> transactions;

    private LocalDateTime closedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
