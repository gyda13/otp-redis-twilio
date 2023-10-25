package com.example.otpredistwilio.services;

import com.example.otpredistwilio.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private final TwilioConfig twilioConfig;

    @Autowired
    public TwilioService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }

    public void sendOTP(String phoneNumber, String otpCode) {
        Message message = Message.creator(
                        new PhoneNumber("whatsapp:"+phoneNumber),
                        new PhoneNumber(twilioConfig.getPhoneNumber()),
                        "Your OTP is: " + otpCode)
                .create();
    }
}
