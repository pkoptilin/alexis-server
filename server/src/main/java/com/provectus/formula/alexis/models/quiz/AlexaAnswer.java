package com.provectus.formula.alexis.models.quiz;

import java.util.ArrayList;

public class AlexaAnswer {

    private String alexaId;
    private ArrayList<WordAnswer> answers;

    public String getAlexaId() {
        return alexaId;
    }

    public void setAlexaId(String alexaId) {
        this.alexaId = alexaId;
    }

    public ArrayList<WordAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<WordAnswer> answers) {
        this.answers = answers;
    }
}
