package com.logistics.zido.account.management.helpers;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class TransporterHelperResponse {

    private String transporterName;
    private String transporterPhoneNumber;
    private String transporterAddress;
    private String transporterEmailAddress;
    private String accountNumber;
    private String accountReference;
    private String accountName;
    private String currencyCode;
    private String bankName;
    private String bankCode;
    private String reservationReference;
    private boolean accountStatus;
    private boolean transporterStatus;
}
