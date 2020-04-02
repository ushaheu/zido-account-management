package com.logistics.zido.account.management.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@Table(name = "driver_information")
public class DriverInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String driverName;
    @NotNull
    private String driverAddress;
    @NotNull
    private String driverLicenceNumber;
    @NotNull
    private String nextOfKinName;
    @NotNull
    private String nextOfKinPhoneNumber;
    @NotNull
    private String nextOfKinAddress;
    @NotNull
    private String nextOfKinRelationship;
    @NotNull
    private String guarantorName;
    @NotNull
    private String guarantorPhoneNumber;
    @NotNull
    private String guarantorAddress;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfEmployment;
    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private VehicleDetail vehicleDetail;
    private boolean verificationStatus;
}
