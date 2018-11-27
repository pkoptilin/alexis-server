package com.provectus.formula.alexis.models.quiz;

public class WordAnswer {

    private Long rusWordId;
    private String answerWord;
    private boolean answer;

    public Long getRusWordId() {
        return rusWordId;
    }

    public void setRusWordId(Long rusWordId) {
        this.rusWordId = rusWordId;
    }

    public String getAnswerWord() {
        return answerWord;
    }

    public void setAnswerWord(String answerWord) {
        this.answerWord = answerWord;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
