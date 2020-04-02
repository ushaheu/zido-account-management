package com.logistics.zido.account.management.helpers.monnify;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IncomeSplitConfig {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String subAccountCode;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public float feePercentage;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int splitPercentage;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public boolean feeBearer;
}
