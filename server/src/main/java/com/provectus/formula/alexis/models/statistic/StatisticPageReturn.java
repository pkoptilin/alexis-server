package com.provectus.formula.alexis.models.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class StatisticPageReturn {
    @JsonIgnore
    private String status;
    private int numberOfPages;
    private List<WordStatisticReturn> words;

    public StatisticPageReturn(String status) {
        this.status = status;
    }

    public StatisticPageReturn(String status, int numberOfPages, List<WordStatisticReturn> words) {
        this.status = status;
        this.numberOfPages = numberOfPages;
        this.words = words;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public List<WordStatisticReturn> getWords() {
        return words;
    }

    public void setWords(List<WordStatisticReturn> words) {
        this.words = words;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatisticPageReturn that = (StatisticPageReturn) o;

        if (numberOfPages != that.numberOfPages) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        return words != null ? words.equals(that.words) : that.words == null;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + numberOfPages;
        result = 31 * result + (words != null ? words.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StatisticPageReturn{" +
                "status='" + status + '\'' +
                ", numberOfPages=" + numberOfPages +
                ", words=" + words +
                '}';
    }
}
