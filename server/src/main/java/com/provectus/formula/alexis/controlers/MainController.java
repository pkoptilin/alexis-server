package com.provectus.formula.alexis.controlers;

import com.provectus.formula.alexis.models.UserReturn;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import com.provectus.formula.alexis.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@CrossOrigin
public class MainController {

    @Autowired
    private UserService userService;

    @PostMapping("/user_registration")
    public ResponseEntity userRegistration(@RequestBody UsersEntity user) {
        if (userService.registerUser(user))
            return new ResponseEntity(HttpStatus.OK);
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/home")
    public ResponseEntity home(Principal principal) {
        UserReturn logInUser = userService.getLogInUser(principal.getName());
        if (logInUser != null)
            return new ResponseEntity(logInUser, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
