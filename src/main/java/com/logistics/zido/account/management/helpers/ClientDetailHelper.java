package com.logistics.zido.account.management.helpers;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class ClientDetailHelper {

    private String clientName;
    private String clientAddress;
    private String contactPersonName;
    private String contactPhoneNumber;
    private String paymentTerms;
}
