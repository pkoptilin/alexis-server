package com.provectus.formula.alexis.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.stereotype.Component;

import java.util.Objects;

@JsonSerialize
@Component
public class WordReturn {

    private Long id;
    private String enWord;
    private String ruWord;
    private String fileName;

    public WordReturn() {
    }

    public WordReturn(String enWord, String ruWord, String fileName) {
        this.enWord = enWord;
        this.ruWord = ruWord;
        this.fileName = fileName;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnWord() {
        return enWord;
    }

    public void setEnWord(String enWord) {
        this.enWord = enWord;
    }

    public String getRuWord() {
        return ruWord;
    }

    public void setRuWord(String ruWord) {
        this.ruWord = ruWord;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", enWord='" + enWord + '\'' +
                ", ruWord='" + ruWord + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordReturn that = (WordReturn) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(enWord, that.enWord) &&
                Objects.equals(ruWord, that.ruWord) &&
                Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enWord, ruWord, fileName);
    }
}
