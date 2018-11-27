package com.provectus.formula.alexis.controlers;

import com.amazonaws.services.xray.model.Http;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import com.provectus.formula.alexis.services.AlexaAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/alexa/")
public class AlexaController {

    @Autowired
    AlexaAuthService alexaAuthService;

    @CrossOrigin
    @RequestMapping("/userOtp")
    public ResponseEntity<String>  getOneTimePassword(){
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ||
                (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ROLE_ANONYMOUS"))))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        String userOPT = alexaAuthService.createUserOTP();
        return new ResponseEntity<>(userOPT, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/linkUser", method = RequestMethod.POST)
    public ResponseEntity<String> validateAndLinkUser(@RequestBody Map<String, String> params){
        String alexaUserId = params.get("userId");
        String userOTP = params.get("userOTP");

        String username;
        try {
            if (alexaUserId != null && userOTP != null &&
                    ((username = alexaAuthService.validateAndLinkUser(alexaUserId, userOTP)) != null)) {
                return new ResponseEntity<>(username, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Incorrect validation code.", HttpStatus.BAD_REQUEST);
            }
        } catch (AlexaAuthService.UserIsLinkedException exception){
            return new ResponseEntity<>("User with this alexa Id is already linked.", HttpStatus.CONFLICT);
        }
    }

}
