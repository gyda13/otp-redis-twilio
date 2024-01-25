package com.example.otpredistwilio.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendOTPRequest {

    @NotBlank(message = "mobile number required")
    @Pattern(regexp = "^\\+9665\\d{8}$", message = "An invalid mobile number has been provided")
    private String phoneNumber;

}
