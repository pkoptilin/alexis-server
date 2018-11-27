package com.provectus.formula.alexis.models;

import java.util.Objects;

public class GroupReturn {
    private Long id;
    private String name;
    private Boolean activeState;
    private Long userId;

    public GroupReturn(String name, Boolean activeState) {
        this.name = name;
        this.activeState = activeState;
    }

    public GroupReturn() {
        this.id = Long.valueOf(0);
        this.userId = Long.valueOf(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setActiveState(Boolean activeState) {
        this.activeState = activeState;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupReturn that = (GroupReturn) o;
        return id.equals(that.id) &&
                userId == that.userId &&
                Objects.equals(name, that.name) &&
                Objects.equals(activeState, that.activeState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, activeState, userId);
    }
}
