package com.provectus.formula.alexis.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.provectus.formula.alexis.models.WordReturn;

import javax.persistence.*;
import java.util.List;

@JsonSerialize
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Entity
@Table(name = "WORD_GROUP")
public class GroupEntity {

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Basic
    @Column(name = "NAME", length = 150)
    private String name;
    @Basic
    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean activeState;

    @Basic
    @Column(name = "ID_USER", length = 150)
    private Long userId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "wordGroup")
    private List<WordEntity> words;

    public GroupEntity() {
    }

    public GroupEntity(String name, Boolean activeState, List<WordEntity> words) {
        this.name = name;
        this.activeState = activeState;
        this.words = words;
    }

    public GroupEntity(String name, Boolean activeState) {
        this.name = name;
        this.activeState = activeState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Boolean getActiveState() {
        return activeState;
    }

    public void setActiveState(Boolean state) {
        this.activeState = state;
    }


    public List<WordEntity> getWords() {
        return words;
    }


    public void setWords(List<WordEntity> words) {
        this.words = words;
    }

    public void addWord(WordEntity word) {
        this.words.add(word);
    }

    public void deleteWord(WordReturn word) {
        this.words.remove(word);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupEntity that = (GroupEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return activeState != null ? activeState.equals(that.activeState) : that.activeState == null;
    }

    @Override
    public int hashCode() {
        int result = id.intValue();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (activeState != null ? activeState.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WordGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", activeState=" + activeState +
                ", userId=" + userId +
                ", words=" + words.toString() +
                '}';
    }


}
