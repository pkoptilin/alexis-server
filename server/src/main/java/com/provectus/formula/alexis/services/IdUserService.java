package com.provectus.formula.alexis.services;

import com.provectus.formula.alexis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdUserService {
    @Autowired
    private UserRepository userRepository;

    public Long findIdUserByMail(String mail) {
        Long out = Long.valueOf(-1);
        if (mail != null) {
            out = Long.valueOf(userRepository.findByEmail(mail).getId());
        }

        // System.out.println(" ########### ID FIND USER " + mail + " :" + out + " ########## ");
        return out;
    }

}
