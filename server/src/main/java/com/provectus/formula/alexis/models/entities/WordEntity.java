package com.provectus.formula.alexis.models.entities;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

@JsonSerialize
@Entity
@Table(name = "WORD_RELATION")

public class WordEntity {
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "ID_RU_WORD", nullable = false)
    private RuWordEntity ruWordEntity;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "ID_WORD", nullable = false)
    private EnWordEntity enWordEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "ID_GROUP", nullable = false)
    private GroupEntity wordGroup;

    public GroupEntity getWordGroup() {
        return wordGroup;
    }

    public void setWordGroup(GroupEntity wordGroup) {
        this.wordGroup = wordGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RuWordEntity getRuWordEntity() {
        return ruWordEntity;
    }

    public void setRuWordEntity(RuWordEntity ruWordEntity) {
        this.ruWordEntity = ruWordEntity;
    }

    public EnWordEntity getEnWordEntity() {
        return enWordEntity;
    }

    public void setEnWordEntity(EnWordEntity enWordEntity) {
        this.enWordEntity = enWordEntity;
    }

}
