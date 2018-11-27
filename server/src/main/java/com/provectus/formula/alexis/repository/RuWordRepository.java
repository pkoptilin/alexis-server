package com.provectus.formula.alexis.repository;

import com.provectus.formula.alexis.models.entities.RuWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface RuWordRepository extends JpaRepository<RuWordEntity, Long> {

    RuWordEntity getById(@Param("id") Long id);

    RuWordEntity getByRuWord(@Param("ruWordEntity") String ruWord);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM RUS_WORD WHERE ID = ?1",
            nativeQuery = true)
    void delById(long wordGroupId);

    @Modifying
    @Query("SELECT r.ruWord FROM RuWordEntity r WHERE r.ruWord LIKE CONCAT(:ruWord,'%') order by r.ruWord asc")
    List<String> findAllByRuWordLike(@Param("ruWord") String ruWord);

}
