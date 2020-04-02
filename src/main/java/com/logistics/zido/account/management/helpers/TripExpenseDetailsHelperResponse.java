package com.logistics.zido.account.management.helpers;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class TripExpenseDetailsHelperResponse {

    private TripDetailHelperResponse tripDetailHelperResponse;
    private TripExpenseInformation tripExpenseInformation;
}
