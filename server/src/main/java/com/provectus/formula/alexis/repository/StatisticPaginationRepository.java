package com.provectus.formula.alexis.repository;

import com.provectus.formula.alexis.models.entities.StatisticEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StatisticPaginationRepository extends PagingAndSortingRepository<StatisticEntity, Long> {

    @Query(value = "SELECT " +
            "  STATISTIC.* " +
            "FROM ((STATISTIC " +
            "  INNER JOIN WORD_RELATION " +
            "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN RUS_WORD " +
            "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD_GROUP " +
            "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
            "  WHERE WORD_GROUP.ID_USER = ?1 " +
            "  AND STATISTIC.CORRECT_ANSWERS >= ?2 " +
            "ORDER BY STATISTIC.ALL_ANSWERS DESC",
            countQuery = "SELECT COUNT(*) " +
                    "FROM ((STATISTIC " +
                    "  INNER JOIN WORD_RELATION " +
                    "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN RUS_WORD " +
                    "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD_GROUP " +
                    "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
                    "WHERE WORD_GROUP.ID_USER = ?1 " +
                    "      AND STATISTIC.CORRECT_ANSWERS >= ?2",
            nativeQuery = true)
    Page<StatisticEntity> getLearnedStatisticPageByUserId(long userId, int isLearned, Pageable pageable);

    @Query(value = "SELECT " +
            "  STATISTIC.* " +
            "FROM ((STATISTIC " +
            "  INNER JOIN WORD_RELATION " +
            "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN RUS_WORD " +
            "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD_GROUP " +
            "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
            "  WHERE WORD_GROUP.ID = ?1 " +
            "  AND STATISTIC.CORRECT_ANSWERS >= ?2 " +
            "ORDER BY STATISTIC.ALL_ANSWERS DESC",
            countQuery = "SELECT COUNT(*) " +
                    "FROM ((STATISTIC " +
                    "  INNER JOIN WORD_RELATION " +
                    "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN RUS_WORD " +
                    "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD_GROUP " +
                    "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
                    "WHERE WORD_GROUP.ID = ?1 " +
                    "      AND STATISTIC.CORRECT_ANSWERS >= ?2",
            nativeQuery = true)
    Page<StatisticEntity> getLearnedStatisticPageByGroupId(long groupId, int isLearned, Pageable pageable);

    @Query(value = "SELECT " +
            "  STATISTIC.* " +
            "FROM ((STATISTIC " +
            "  INNER JOIN WORD_RELATION " +
            "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN RUS_WORD " +
            "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD_GROUP " +
            "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
            "  WHERE WORD_GROUP.ID_USER = ?1 " +
            "  AND STATISTIC.CORRECT_ANSWERS < ?2 " +
            "ORDER BY STATISTIC.ALL_ANSWERS DESC",
            countQuery = "SELECT COUNT(*) " +
                    "FROM ((STATISTIC " +
                    "  INNER JOIN WORD_RELATION " +
                    "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN RUS_WORD " +
                    "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD_GROUP " +
                    "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
                    "WHERE WORD_GROUP.ID_USER = ?1 " +
                    "      AND STATISTIC.CORRECT_ANSWERS < ?2",
            nativeQuery = true)
    Page<StatisticEntity> getInProgressStatisticPageByUserId(long userId, int isLearned, Pageable pageable);

    @Query(value = "SELECT " +
            "  STATISTIC.* " +
            "FROM ((STATISTIC " +
            "  INNER JOIN WORD_RELATION " +
            "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN RUS_WORD " +
            "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD_GROUP " +
            "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
            "  WHERE WORD_GROUP.ID = ?1 " +
            "  AND STATISTIC.CORRECT_ANSWERS < ?2 " +
            "ORDER BY STATISTIC.ALL_ANSWERS DESC",
            countQuery = "SELECT COUNT(*) " +
                    "FROM ((STATISTIC " +
                    "  INNER JOIN WORD_RELATION " +
                    "    ON STATISTIC.WORD_RELATION_ID = WORD_RELATION.ID) INNER JOIN RUS_WORD " +
                    "    ON RUS_WORD.ID = WORD_RELATION.ID_RU_WORD) INNER JOIN WORD_GROUP " +
                    "    ON WORD_RELATION.ID_GROUP = WORD_GROUP.ID " +
                    "WHERE WORD_GROUP.ID = ?1 " +
                    "      AND STATISTIC.CORRECT_ANSWERS < ?2",
            nativeQuery = true)
    Page<StatisticEntity> getInProgressStatisticPageByGroupId(long groupId, int isLearned, Pageable pageable);

}
