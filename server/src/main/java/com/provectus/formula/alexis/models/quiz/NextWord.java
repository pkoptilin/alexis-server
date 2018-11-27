package com.provectus.formula.alexis.models.quiz;

import java.util.List;

public class NextWord {
    private Long wordId;
    private String word;
    private String audioFileName;
    private List<String> answers;

    public NextWord(Long wordId, String word, String audioFileName, List<String> answers) {
        this.wordId = wordId;
        this.word = word;
        this.audioFileName = audioFileName;
        this.answers = answers;
    }



    public Long getWordId() {
        return wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
