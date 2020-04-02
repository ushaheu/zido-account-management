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
@Table(name = "vehicle_make")
public class VehicleMake {

    @Id
    private String vehicleMake;
    private String vehicleManufacturer;
    @OneToOne
    @JoinColumn(name = "vehicle_type")
    private VehicleType vehicleType;
}
