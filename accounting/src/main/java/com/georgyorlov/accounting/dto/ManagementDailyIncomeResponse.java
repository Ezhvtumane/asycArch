package com.georgyorlov.accounting.dto;

import com.georgyorlov.accounting.entity.BillingStatus;
import lombok.Data;

@Data
public class ManagementDailyIncomeResponse {

    private Long income;
    private BillingStatus billingStatus;

}
