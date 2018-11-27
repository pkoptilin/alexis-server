package com.provectus.formula.alexis.models.entities;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.List;

@JsonSerialize
@Entity
@Table(name = "RUS_WORD")
public class RuWordEntity {


    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Basic
    @Column(name = "RU_WORD", length = 150, unique = true)
    private String ruWord;
    @Basic
    @Column(name = "FILE_NAME", length = 150)
    private String fileName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ruWordEntity")
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

}
