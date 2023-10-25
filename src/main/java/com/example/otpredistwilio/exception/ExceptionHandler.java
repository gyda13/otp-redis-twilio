package com.example.otpredistwilio.exception;

import com.example.otpredistwilio.exception.model.CustomException;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

@Slf4j
@Singleton
public class ExceptionHandler extends Throwable {

    public static CustomException handleException(Throwable throwable) {
        log.error("Exception occurred: ", throwable);
        CustomException ex = new CustomException(throwable.getMessage());
        return ex;
    }

}
