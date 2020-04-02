package com.logistics.zido.account.management.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@Table(name = "vehicle_detail")
public class VehicleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vehicleNumber;
    @OneToOne
    @JoinColumn(name = "vehicle_make_id")
    private VehicleMake vehicleMake;
    private String chasisNumber;
    private String engineNumber;
    private String yearOfManufacture;
    private String yearOfPurchase;
    private String tonnage;
    @OneToOne
    @JoinColumn(name = "transporter_id")
    private Transporter transporter;
    private boolean driverAssignedStatus;
}
