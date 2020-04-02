package com.logistics.zido.account.management.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@Table(name = "vehicle_type")
public class VehicleType {

    @Id
    private String vehicleType;
    private String vehicleDescription;
}
