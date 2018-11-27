package com.provectus.formula.alexis.services;

import com.provectus.formula.alexis.DAO.ConfigurationDAO;
import com.provectus.formula.alexis.models.ConfigurationReturn;
import com.provectus.formula.alexis.models.entities.ConfigurationEntity;
import com.provectus.formula.alexis.models.entities.UsersEntity;
import com.provectus.formula.alexis.repository.ConfigurationRepository;
import com.provectus.formula.alexis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigurationService implements ConfigurationDAO {
    @Autowired
    private ConfigurationRepository configurationRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ConfigurationReturn getConfigurationByUserId(Long userId) {
        ConfigurationReturn outSettings = new ConfigurationReturn();
        ConfigurationEntity tmpSettings = configurationRepository.getByUserId(userId);
        outSettings.setFailApproach(tmpSettings.getFailApproach());
        outSettings.setSuccessApproach(tmpSettings.getSuccessApproach());
        outSettings.setDefaultGroupId(tmpSettings.getDefaultGroupId());
        return outSettings;

    }

    @Override
    public void updateConfiguration(ConfigurationReturn configurationReturn, Long userId) {

        ConfigurationEntity updateSettings = new ConfigurationEntity();
        updateSettings.setFailApproach(configurationReturn.getFailApproach());
        updateSettings.setSuccessApproach(configurationReturn.getSuccessApproach());
        updateSettings.setDefaultGroupId(configurationReturn.getDefaultGroupId());
        updateSettings.setUserId(userId);
        updateSettings.setId(configurationRepository.getByUserId(userId).getId());

        configurationRepository.saveAndFlush(updateSettings);
    }

    @Override
    public Optional<ConfigurationReturn> getConfigurationByAlexaId(String alexaId) {
        List<UsersEntity> users = userRepository.findByAwsid(alexaId);
        if (!users.isEmpty()){
            return Optional.of(getConfigurationByUserId((long)users.get(0).getId()));
        }
        return Optional.empty();
    }
}
