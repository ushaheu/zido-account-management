package com.logistics.zido.account.management.helpers;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class TripExpenseDetailsHelperRequest {

    private Long tripDetailId;
    private List<TripExpenseInformation> tripExpenseInformationList;
}
