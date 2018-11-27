package com.provectus.formula.alexis.repository;

import com.provectus.formula.alexis.models.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, Long> {

    @Transactional(readOnly = true)
    List<UsersEntity> findAll();

    @Transactional(readOnly = true)
    UsersEntity findByEmail(String email);

    @Transactional(readOnly = true)
    List<UsersEntity> findByAwsid(String awsId);

    @Transactional(readOnly = true)
    boolean existsByAwsid(String awsId);
}
