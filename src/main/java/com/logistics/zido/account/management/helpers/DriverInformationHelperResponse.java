package com.logistics.zido.account.management.helpers;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class DriverInformationHelperResponse {

    private String driverName;
    private String driverAddress;
    private String driverLicenceNumber;
    private String nextOfKinName;
    private String nextOfKinPhoneNumber;
    private String nextOfKinAddress;
    private String nextOfKinRelationship;
    private String guarantorName;
    private String guarantorPhoneNumber;
    private String guarantorAddress;
    private Date dateOfEmployment;
    private VehicleDetailHelperResponse vehicleDetailHelperResponse;
    private boolean verificationStatus;
}
