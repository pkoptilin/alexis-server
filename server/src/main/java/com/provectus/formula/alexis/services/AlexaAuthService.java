package com.provectus.formula.alexis.services;

import com.provectus.formula.alexis.models.entities.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AlexaAuthService {

    @Autowired
    OTPService otpService;

    @Autowired
    UserService userService;

    public String createUserOTP() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userKey = authentication.getName();
        String userOTP = otpService.createUserOTP(userKey);
        return userOTP;
    }

    public String validateAndLinkUser(String alexaUserId, String userOTP) throws UserIsLinkedException{
        String userKey;
        if (userService.alexaIdIsLinked(alexaUserId))
            throw new UserIsLinkedException();
        if ((userKey = otpService.getUserByOTP(userOTP)) != null) {
            return userService.linkAlexaId(userKey, alexaUserId);
        } else {
            return null;
        }
    }

    public UsersEntity logoutAlexa(String alexaId){
        return userService.logoutAlexa(alexaId);
    }

    public static class UserIsLinkedException extends RuntimeException { }
}
