package com.logistics.zido.account.management.helpers;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class VehicleDetailHelperResponse {

    private String vehicleNumber;
    private VehicleMakeHelperResponse vehicleMakeHelperResponse;
    private String chasisNumber;
    private String engineNumber;
    private String yearOfManufacture;
    private String yearOfPurchase;
    private String tonnage;
    private TransporterHelperResponse transporterHelperResponse;
    private boolean driverAssignedStatus;
}
