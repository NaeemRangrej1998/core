package com.ecommerce.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class CommonUtils {
    public CommonUtils() {
    }

    public static LocalDateTime getDateTime(){
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
