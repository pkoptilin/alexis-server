package com.provectus.formula.alexis.models.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.List;

@JsonSerialize
@Entity
@Table(name = "WORD")
public class EnWordEntity {


    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Basic
    @Column(name = "EN_WORD", length = 150, unique = true)
    private String enWord;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "enWordEntity")
    private List<WordEntity> words;

    public List<WordEntity> getWords() {
        return words;
    }

    public void setWords(List<WordEntity> words) {
        this.words = words;
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

}
