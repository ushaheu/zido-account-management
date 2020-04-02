package com.logistics.zido.account.management.helpers.monnify;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ReserveAccountRequest {

    public String accountReference;
    public String accountName;
    public String currencyCode;
    public String contractCode;
    public String customerEmail;
    public List<IncomeSplitConfig> incomeSplitConfig;
}
