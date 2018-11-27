package com.provectus.formula.alexis.repository;

import com.provectus.formula.alexis.models.entities.WordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)

public interface WordRepository extends JpaRepository<WordEntity, Long> {

    WordEntity getById(@Param("id") Long id);

    WordEntity findByEnWordEntity(@Param("enWord") String enWord);

    WordEntity findByRuWordEntity(@Param("ruWord") String ruWord);

    WordEntity findByWordGroupId(@Param("wordGroupId") Long wordGroupId);


    @Query(value = "SELECT COUNT(*) FROM WORD_RELATION WHERE ID_GROUP = ?1 and ID_WORD=?2 and ID_RU_WORD=?3",
            nativeQuery = true)
    int countWordsInCurrentGroup(long idGroup, long idEnWord, long idRuWord);

    @Query(value = "SELECT ID FROM WORD_RELATION WHERE ID_GROUP = ?1 and ID_WORD=?2 and ID_RU_WORD=?3",
            nativeQuery = true)
    long idWordsInCurrentGroup(long idGroup, long idEnWord, long idRuWord);

    @Query(value = "SELECT COUNT(*) FROM WORD_RELATION WHERE  ID_WORD=?1",
            nativeQuery = true)
    int countEnWordInGroup(long idEnWord);

    @Query(value = "SELECT COUNT(*) FROM WORD_RELATION WHERE ID_RU_WORD=?1",
            nativeQuery = true)
    int countRuWordInGroup(long idRuWord);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM WORD_RELATION WHERE ID_GROUP = ?1 AND ID = ?2 ",
            nativeQuery = true)
    void deleteWordsFromGroup(long wordGroupId, long wordId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM WORD_RELATION WHERE ID_GROUP = ?1",
            nativeQuery = true)
    void deleteAllWordsFromGroup(long wordGroupId);


    @Transactional
    @Modifying
    @Query(value = "insert into WORD_RELATION(ID,ID_GROUP,ID_WORD,ID_RU_WORD)values (null,?1,?2,?3)",
            nativeQuery = true)
    void insertWordsInGroup(long idGroup, long idEnWord, long idRuWord);


    @Query(value = "SELECT COUNT(*) " +
            "FROM (WORD_RELATION INNER JOIN WORD_GROUP " +
            "ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID) " +
            "INNER JOIN USERS " +
            "ON WORD_GROUP.ID_USER = USERS.ID " +
            "WHERE USERS.ID = ?1", nativeQuery = true)
    int countAllUsersWords(Long userID);

    @Query(value = "SELECT WORD_RELATION.ID " +
            "FROM ((STATISTIC " +
            "  INNER JOIN WORD_RELATION " +
            "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN WORD_GROUP " +
            "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID) INNER JOIN USERS " +
            "    ON WORD_GROUP.ID_USER = USERS.ID " +
            "WHERE WORD_GROUP.IS_ACTIVE = TRUE " +
            "      AND USERS.ID = ?1 " +
            "ORDER BY STATISTIC.QUIZ_COUNT ASC, random() " +
            "LIMIT 5", nativeQuery = true)
    long[] findWordIDByTheSmallestStatistic(Long userID);

    @Query(value = "SELECT WORD_RELATION.ID " +
            "FROM ((STATISTIC " +
            "  INNER JOIN WORD_RELATION " +
            "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN WORD_GROUP " +
            "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID) INNER JOIN USERS " +
            "    ON WORD_GROUP.ID_USER = USERS.ID " +
            "WHERE WORD_GROUP.IS_ACTIVE = TRUE " +
            "      AND USERS.ID = ?2 " +
            "      AND WORD_GROUP.ID = ?1 " +
            "ORDER BY STATISTIC.QUIZ_COUNT ASC, random() " +
            "LIMIT 5", nativeQuery = true)
    long[] findWordIDByTheWordGroupAndSmallestStatistic(Long wordGroup, Long userID);

    @Query(value = "SELECT COUNT(*) " +
            "FROM WORD_RELATION " +
            "  INNER JOIN WORD_GROUP " +
            "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
            "WHERE WORD_GROUP.ID = ?1", nativeQuery = true)
    int countWordsInGroup(Long wordGroup);

    @Query(value = "SELECT DISTINCT WORD.EN_WORD " +
            "FROM (RUS_WORD " +
            "  INNER JOIN WORD_RELATION " +
            "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD " +
            "    ON WORD_RELATION.ID_WORD = WORD.ID " +
            "WHERE RUS_WORD.RU_WORD = ( " +
            "  SELECT RUS_WORD.RU_WORD " +
            "  FROM WORD_RELATION " +
            "    INNER JOIN RUS_WORD " +
            "      ON WORD_RELATION.ID_RU_WORD = RUS_WORD.ID " +
            "  WHERE WORD_RELATION.ID = ?1 " +
            ")", nativeQuery = true)
    String[] findAnswersByWordId(Long wordId);
}
