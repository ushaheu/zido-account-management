package com.logistics.zido.account.management.helpers;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class VehicleDetailHelperRequest {

    private String vehicleNumber;
    private String vehicleMakeId;
    private String chasisNumber;
    private String engineNumber;
    private String yearOfManufacture;
    private String yearOfPurchase;
    private String tonnage;
    private Long transporterId;
    private boolean driverAssignedStatus;
}
