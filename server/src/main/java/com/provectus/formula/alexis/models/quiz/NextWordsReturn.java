package com.provectus.formula.alexis.models.quiz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
@JsonSerialize
public class NextWordsReturn {
    @JsonIgnore
    private String status;
    private List<NextWord> answers;

    public NextWordsReturn(String status, List<NextWord> answers) {
        this.status = status;
        this.answers = answers;
    }

    public NextWordsReturn(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NextWord> getAnswers() {
        return answers;
    }

    public void setAnswers(List<NextWord> answers) {
        this.answers = answers;
    }
}
