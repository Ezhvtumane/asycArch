package com.georgyorlov.accounting.dto;

import com.georgyorlov.accounting.entity.Account;
import com.georgyorlov.accounting.entity.TransactionEntity;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAccountResponse {

    private Account account;
    private List<TransactionEntity> transactionEntityList;
}
