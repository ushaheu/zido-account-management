package com.logistics.zido.account.management.entities;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@Table(name = "trip_detail")
public class TripDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfRequest;
    @OneToOne
    @JoinColumn(name = "driver_id")
    private DriverInformation driverInformation;
    @NotNull
    private String pickUpLocationAddress;
    @NotNull
    private String destinationAddress;
    @NotNull
    private String wayBillNumber;
    @NotNull
    private BigDecimal invoiceAmount;
}
