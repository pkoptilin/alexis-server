package com.provectus.formula.alexis.repository;

import com.provectus.formula.alexis.models.entities.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long> {
    ConfigurationEntity getByUserId(@Param("userId") Long userId);

}
