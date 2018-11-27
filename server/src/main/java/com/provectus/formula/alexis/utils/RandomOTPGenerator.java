package com.provectus.formula.alexis.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class RandomOTPGenerator implements OTPGenerator {
    @Value("${OTP_length:6}")
    private Integer OTP_length;

    public String getOTP() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        int m = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);
        int ms = cal.get(Calendar.MILLISECOND);

        int v = (m % 10) * 1235 + s * 79 + ms * 982 + 1;
        String ret = String.format("%0" + OTP_length + "d", v);
        return ret;
    }

}
