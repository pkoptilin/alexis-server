package com.provectus.formula.alexis.services;

import com.provectus.formula.alexis.models.UserReturn;
import com.provectus.formula.alexis.models.entities.ConfigurationEntity;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import com.provectus.formula.alexis.repository.ConfigurationRepository;
import com.provectus.formula.alexis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfigurationRepository configurationRepository;

    public String linkAlexaId(String userKey, String alexaUserId) {
        UsersEntity user = userRepository.findByEmail(userKey);
        if (user != null) {
            user.setAwsid(alexaUserId);
            UsersEntity updated = userRepository.save(user);
            return updated.getAwsid().equals(alexaUserId) ? updated.getName() : null;
        }
        return null;
    }

    public boolean registerUser(UsersEntity user) {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            UsersEntity currentUser = userRepository.save(new UsersEntity(user.getEmail(),
                    passwordEncoder.encode(user.getPassword()), user.getName()));
            ConfigurationEntity currentConfiguration = new ConfigurationEntity();
            currentConfiguration.setUserId(Long.valueOf(currentUser.getId()));
            configurationRepository.saveAndFlush(currentConfiguration);

            return true;
        }
        return false;
    }

    public UserReturn getLogInUser(String userLog) {
        UsersEntity user = userRepository.findByEmail(userLog);
        if (user == null) {
            return null;
        } else {
            UserReturn userReturn = new UserReturn(user.getName(), user.getEmail());
            if (user.getAwsid() == null) {
                userReturn.setAwsExist(false);
            } else {
                userReturn.setAwsExist(true);
            }
            return userReturn;
        }
    }

    public boolean alexaIdIsLinked(String alexaId) {
        return userRepository.existsByAwsid(alexaId);
    }

    public UsersEntity logoutAlexa(String alexaId) {
        List<UsersEntity> users = userRepository.findByAwsid(alexaId);
        if (!users.isEmpty()) {
            UsersEntity user = users.get(0);
            user.setAwsid(null);
            userRepository.save(user);
            return user;
        }
        return null;
    }
}
