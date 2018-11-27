package com.provectus.formula.alexis.DAO;

import com.provectus.formula.alexis.models.ConfigurationReturn;

import java.util.Optional;

public interface ConfigurationDAO {
    ConfigurationReturn getConfigurationByUserId(Long userId);
    Optional<ConfigurationReturn> getConfigurationByAlexaId(String alexaId);
    void updateConfiguration(ConfigurationReturn configurationReturn, Long userId);
}
