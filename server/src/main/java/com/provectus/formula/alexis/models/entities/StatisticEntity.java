package com.provectus.formula.alexis.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "STATISTIC")
public class StatisticEntity {
    private long id;
    private Long correctAnswers;
    private Long allAnswers;
    private Long wordRelationId;
    private Long quizCount;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CORRECT_ANSWERS", nullable = true)
    public Long getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Long correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    @Basic
    @Column(name = "ALL_ANSWERS", nullable = true)
    public Long getAllAnswers() {
        return allAnswers;
    }

    public void setAllAnswers(Long allAnswers) {
        this.allAnswers = allAnswers;
    }

    @Basic
    @Column(name = "WORD_RELATION_ID", nullable = true)
    public Long getWordRelationId() {
        return wordRelationId;
    }

    public void setWordRelationId(Long wordRelationId) {
        this.wordRelationId = wordRelationId;
    }

    @Basic
    @Column(name = "QUIZ_COUNT", nullable = true)
    public Long getQuizCount() {
        return quizCount;
    }

    public void setQuizCount(Long quizCount) {
        this.quizCount = quizCount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatisticEntity that = (StatisticEntity) o;

        if (id != that.id) return false;
        if (correctAnswers != null ? !correctAnswers.equals(that.correctAnswers) : that.correctAnswers != null)
            return false;
        if (allAnswers != null ? !allAnswers.equals(that.allAnswers) : that.allAnswers != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (correctAnswers != null ? correctAnswers.hashCode() : 0);
        result = 31 * result + (allAnswers != null ? allAnswers.hashCode() : 0);
        return result;
    }
}
