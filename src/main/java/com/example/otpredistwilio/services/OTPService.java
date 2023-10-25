package com.example.otpredistwilio.services;

import com.example.otpredistwilio.models.responses.SendOTPResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class OTPService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TwilioService twilioService;

    private static final String OTP_PREFIX = "otp:";
    private static final String RATE_LIMIT_PREFIX = "rate_limit:";
    private static final int RATE_LIMIT = 5;
    private static final long RATE_LIMIT_PERIOD_SECONDS = 60;
    private static final int CODE_LENGTH = 6;

    private static final int TIME_TO_LIVE = 2;


    public SendOTPResponse generateOTP(String phoneNumber) {
        // Check rate limiting
        String rateLimitKey = RATE_LIMIT_PREFIX + phoneNumber;
        if (!isRateLimited(rateLimitKey)) {
            throw new RuntimeException("Rate limit exceeded for phone number: " + phoneNumber);
        }

        // Generate OTP
        String otpKey = OTP_PREFIX + phoneNumber;
        String existingOTP = redisTemplate.opsForValue().get(otpKey);
        if (existingOTP != null) {
            long remainingTime = redisTemplate.getExpire(otpKey, TimeUnit.SECONDS);
            if (remainingTime > 0) {
                throw new RuntimeException("An OTP has already been generated for this number: " + phoneNumber +
                        ". OTP will expire in " + remainingTime + " seconds.");
            }
        }

        String otpCode = generateRandomOTP(CODE_LENGTH);
        redisTemplate.opsForValue().set(otpKey, otpCode, Duration.ofMinutes(TIME_TO_LIVE));

        twilioService.sendOTP(phoneNumber, otpCode);

        SendOTPResponse sendOTPResponse = new
                SendOTPResponse(
                phoneNumber,
                CODE_LENGTH, TIME_TO_LIVE);
        return sendOTPResponse;
    }

    public boolean verifyOTP(String phoneNumber, String otpCode) {
        String otpKey = OTP_PREFIX + phoneNumber;
        String storedOTP = redisTemplate.opsForValue().get(otpKey);

        if (storedOTP != null && storedOTP.equals(otpCode)) {
            redisTemplate.delete(otpKey);
            return true;
        }

        return false;
    }

    private boolean isRateLimited(String rateLimitKey) {
        Long currentCount = redisTemplate.opsForValue().increment(rateLimitKey, 1);
        if (currentCount == 1) {
            redisTemplate.expire(rateLimitKey, RATE_LIMIT_PERIOD_SECONDS, TimeUnit.SECONDS);
        }
        return currentCount <= RATE_LIMIT;
    }

    private String generateRandomOTP(int length) {
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(numbers.length());
            otp.append(numbers.charAt(index));
        }

        return otp.toString();
    }

}