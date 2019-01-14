package com.provectus.formula.alexis.repository;

import com.provectus.formula.alexis.models.entities.StatisticEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface StatisticRepository extends JpaRepository<StatisticEntity, Long> {

    StatisticEntity findByWordRelationId(@Param("wordRelationId") Long wordRelationId);

    @Transactional
    @Modifying
    @Query(value = "insert into STATISTIC(ALL_ANSWERS,CORRECT_ANSWERS, QUIZ_COUNT, WORD_RELATION_ID)values (0,0,0,?1);",
            nativeQuery = true)
    void insertStatisticInWord(long worldRelationID);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM STATISTIC WHERE WORD_RELATION_ID = ?1",
            nativeQuery = true)
    void deleteStatisticByWordRelationId(long worldRelationID);

    @Transactional
    @Modifying
    @Query(value = "UPDATE STATISTIC " +
            "SET STATISTIC.QUIZ_COUNT = 0 " +
            "WHERE STATISTIC.WORD_RELATION_ID IN (SELECT WORD_RELATION.ID " +
            "                                     FROM (WORD_RELATION INNER JOIN WORD_GROUP " +
            "                                       ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID) " +
            "                                       INNER JOIN USERS " +
            "                                       ON WORD_GROUP.ID_USER = USERS.ID " +
            "                                     WHERE USERS.ID = ?1);",
            nativeQuery = true)
    void cleanQuizByUserId(long userId);

    @Transactional
    @Query(value = "SELECT COUNT(*) " +
            "FROM ((STATISTIC " +
            "  INNER JOIN WORD_RELATION " +
            "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN RUS_WORD " +
            "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD_GROUP " +
            "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
            "WHERE WORD_GROUP.ID_USER = ?1 " +
            "      AND STATISTIC.CORRECT_ANSWERS >= ?2",
            nativeQuery = true)
    int countLearnedStatisticByUserId(long userId, int isLearned);

    @Transactional
    @Query(value = "SELECT COUNT(*) " +
            "FROM ((STATISTIC " +
            "  INNER JOIN WORD_RELATION " +
            "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN RUS_WORD " +
            "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD_GROUP " +
            "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
            "WHERE WORD_GROUP.ID = ?1 " +
            "      AND STATISTIC.CORRECT_ANSWERS >= ?2",
            nativeQuery = true)
    int countLearnedStatisticByGroupId(long groupId, int isLearned);

    @Transactional
    @Query(value = "SELECT COUNT(*) " +
            "FROM ((STATISTIC " +
            "  INNER JOIN WORD_RELATION " +
            "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN RUS_WORD " +
            "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD_GROUP " +
            "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
            "WHERE WORD_GROUP.ID_USER = ?1 " +
            "      AND STATISTIC.CORRECT_ANSWERS < ?2",
            nativeQuery = true)
    int countInProgressStatisticByUserId(long userId, int isLearned);

    @Transactional
    @Query(value = "SELECT COUNT(*) " +
            "FROM ((STATISTIC " +
            "  INNER JOIN WORD_RELATION " +
            "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN RUS_WORD " +
            "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD_GROUP " +
            "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
            "WHERE WORD_GROUP.ID = ?1 " +
            "      AND STATISTIC.CORRECT_ANSWERS < ?2",
            nativeQuery = true)
    int countInProgressStatisticByGroupId(long groupId, int isLearned);
}
