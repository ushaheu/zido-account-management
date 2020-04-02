package com.logistics.zido.account.management.helpers;

import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class TripDetailHelperRequest {

    private Date dateOfRequest;
    private Long driverInformationId;
    private String pickUpLocationAddress;
    private String destinationAddress;
    private String wayBillNumber;
    private BigDecimal invoiceAmount;
}
