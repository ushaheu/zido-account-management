package com.logistics.zido.account.management.helpers.monnify.authentication;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class AuthenticationResponse {

    public boolean requestSuccessful;
    public String responseMessage;
    public String responseCode;
    public ResponseBody responseBody;
}
