package com.provectus.formula.alexis.models.statistic;

public class WordStatisticReturn {
    private String ruWord;
    private Long correct;
    private long wrong;

    public WordStatisticReturn(String ruWord, Long correct, long wrong) {
        this.ruWord = ruWord;
        this.correct = correct;
        this.wrong = wrong;
    }

    public String getRuWord() {
        return ruWord;
    }

    public void setRuWord(String ruWord) {
        this.ruWord = ruWord;
    }

    public Long getCorrect() {
        return correct;
    }

    public void setCorrect(Long correct) {
        this.correct = correct;
    }

    public long getWrong() {
        return wrong;
    }

    public void setWrong(long wrong) {
        this.wrong = wrong;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordStatisticReturn that = (WordStatisticReturn) o;

        if (wrong != that.wrong) return false;
        if (ruWord != null ? !ruWord.equals(that.ruWord) : that.ruWord != null) return false;
        return correct != null ? correct.equals(that.correct) : that.correct == null;
    }

    @Override
    public int hashCode() {
        int result = ruWord != null ? ruWord.hashCode() : 0;
        result = 31 * result + (correct != null ? correct.hashCode() : 0);
        result = 31 * result + (int) (wrong ^ (wrong >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "WordStatisticReturn{" +
                "ruWord='" + ruWord + '\'' +
                ", correct=" + correct +
                ", wrong=" + wrong +
                '}';
    }
}
