package com.provectus.formula.alexis.repository;

import com.provectus.formula.alexis.models.entities.EnWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface EnWordRepository extends JpaRepository<EnWordEntity, Long> {

    EnWordEntity getById(@Param("id") Long id);

    EnWordEntity getByEnWord(@Param("enWordEntity") String enWord);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM WORD WHERE ID = ?1",
            nativeQuery = true)
    void delById(long wordGroupId);

    @Modifying
    @Query("SELECT e.enWord FROM EnWordEntity e WHERE e.enWord LIKE CONCAT(:enWord,'%') order by e.enWord asc")
    List<String> findAllByEnWordLike(@Param("enWord") String enWord);
    // @Modifying
    // @Query("SELECT EN_WORD FROM WORD WHERE EN_WORD LIKE (:?1,'%') order by EN_WORD asc",
    //         nativeQuery = true)
    // List<String> findAllByEnWordIsLike(String enWord);
    // List<String> findTopByEnWordLike(String enWord);

}
