package com.logistics.zido.account.management.helpers;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class TripExpenseInformation {

    private String expenseId;
    private BigDecimal expenseAmount;
}
