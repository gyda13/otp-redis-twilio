package com.example.otpredistwilio.controllers;

import com.example.otpredistwilio.exception.ExceptionHandler;
import com.example.otpredistwilio.exception.model.CustomException;
import com.example.otpredistwilio.models.requests.SendOTPRequest;
import com.example.otpredistwilio.models.requests.VerifyOTPRequest;
import com.example.otpredistwilio.models.responses.SendOTPResponse;
import com.example.otpredistwilio.models.responses.VerifyOTPResponse;
import com.example.otpredistwilio.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
public class OTPController {

    @Autowired
    private OTPService otpService;


    @PostMapping ("/send")
    public ResponseEntity<?> sendOTP(@RequestBody SendOTPRequest sendOTPRequest) {
        try {
            SendOTPResponse result = otpService.generateOTP(sendOTPRequest.getPhoneNumber());
            return ResponseEntity.ok(result);
        }
        catch (Throwable e) {
            CustomException errorMessage = ExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<VerifyOTPResponse> verifyOTP(@RequestBody VerifyOTPRequest verifyOTPRequest) {
        boolean isValid = otpService.verifyOTP(verifyOTPRequest.getPhoneNumber(), verifyOTPRequest.getOtpCode());
        if (isValid) {
            VerifyOTPResponse verifyOTPResponse = new VerifyOTPResponse("OTP verification successful");
            return ResponseEntity.ok(verifyOTPResponse);
        } else {
            VerifyOTPResponse verifyOTPResponse = new VerifyOTPResponse("Invalid OTP");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(verifyOTPResponse);
        }
    }


}