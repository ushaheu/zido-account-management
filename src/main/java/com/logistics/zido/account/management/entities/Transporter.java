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
@Table(name = "transporter")
public class Transporter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transporterName;
    private String transporterPhoneNumber;
    private String transporterAddress;
    private String transporterEmailAddress;
    private String accountNumber;
    private String accountReference;
    private String accountName;
    private String currencyCode;
    private String bankName;
    private String bankCode;
    private String reservationReference;
    private boolean accountStatus;
    private boolean transporterStatus;
}
