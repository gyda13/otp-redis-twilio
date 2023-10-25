package com.example.otpredistwilio.models.responses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendOTPResponse {

    private String phoneNumber;
    private int codeLength;
    private long timeToLive;
}
