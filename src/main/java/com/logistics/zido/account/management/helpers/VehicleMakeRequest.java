package com.logistics.zido.account.management.helpers;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class VehicleMakeRequest {

    private String vehicleMake;
    private String vehicleManufacturer;
    private String vehicleTypeId;
}
