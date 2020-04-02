package com.logistics.zido.account.management.helpers.monnify.response;

import com.logistics.zido.account.management.helpers.monnify.IncomeSplitConfig;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class ResponseBody {

    public String contractCode;
    public String accountReference;
    public String accountName;
    public String currencyCode;
    public String customerEmail;
    public String accountNumber;
    public String bankName;
    public String bankCode;
    public String reservationReference;
    public String status;
    public String createdOn;
    public List<IncomeSplitConfig> incomeSplitConfig = null;
}
