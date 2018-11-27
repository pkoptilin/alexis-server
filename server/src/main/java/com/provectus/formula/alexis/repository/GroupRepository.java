package com.provectus.formula.alexis.repository;

import com.provectus.formula.alexis.models.entities.GroupEntity;
import com.provectus.formula.alexis.models.entities.WordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    @Override
    GroupEntity getOne(Long id);

    List<GroupEntity> findWordGroupByUserId(@Param("userId") Long userId);

    List<GroupEntity> findWordGroupByUserIdAndActiveState(@Param("userId") Long userId,
                                                          @Param("state") boolean ActiveState);

    GroupEntity findWordGroupByNameAndUserId(@Param("name") String name,
                                             @Param("userId") Long userId);

    GroupEntity findByIdAndUserId(@Param("id") Long GroupId,
                                  @Param("userId") Long userId);

    GroupEntity findWordGroupByName(@Param("name") String name);

    GroupEntity findGroupByIdAndWords(@Param("Id") Long id, @Param("words") WordEntity word);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM WORD_GROUP WHERE ID = ?1",
            nativeQuery = true)
    void delById(long wordGroupId);

}
