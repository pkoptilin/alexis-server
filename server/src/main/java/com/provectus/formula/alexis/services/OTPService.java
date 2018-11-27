package com.provectus.formula.alexis.services;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.provectus.formula.alexis.utils.OTPGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class OTPService {

    @Value("${OTP_length:6}")
    private Integer OTP_length;

    @Value("${OTP_life_time:300}")
    private Integer OTP_life_time;

    @Autowired
    OTPGenerator otpGenerator;

    private LoadingCache<String, String> otpCache;

    @PostConstruct
    public void postConstruct(){
        otpCache = CacheBuilder.newBuilder().maximumSize(1000).
                expireAfterWrite(OTP_life_time, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
                    public String load(String key) {
                        return "";
                    }
                });
    }

    public String createUserOTP(String userKey) {
        String userOTP = otpGenerator.getOTP();
        otpCache.put(userOTP, userKey);
        return userOTP;
    }

    public String getUserByOTP(String userOTP) {
        String OTPinCache = otpCache.getIfPresent(userOTP);
        return OTPinCache;
    }

}
