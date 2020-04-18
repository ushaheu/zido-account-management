package com.logistics.zido.account.management.helpers;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class TransporterDetailHelper {

    private Long id;
    private String transporterName;
    private String transporterPhoneNumber;
    private String transporterAddress;
    private String transporterEmailAddress;
}
