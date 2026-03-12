package com.Nexora.NexoraFinance.transaction.dtos;


import com.Nexora.NexoraFinance.account.dtos.AccountDTO;
import com.Nexora.NexoraFinance.account.entity.Account;
import com.Nexora.NexoraFinance.enums.TransactionStatus;
import com.Nexora.NexoraFinance.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long id;

    private BigDecimal amount;

    private TransactionType transactionType;

    private LocalDateTime transactionDate;

    private String description;

    private TransactionStatus status;

    @JsonBackReference
    private AccountDTO account;

    // for transfer
    private String sourceAccount;
    private String destinationAccount;
}
