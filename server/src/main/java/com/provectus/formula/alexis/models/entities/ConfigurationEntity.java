package com.provectus.formula.alexis.models.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Objects;

@JsonSerialize
@Entity
@Table(name = "CONFIGURATION")
public class ConfigurationEntity {
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Basic
    @Column(name = "ID_USER", length = 150)
    private Long userId;

    @Basic
    @Column(name = "FAIL_APPROACH", length = 150)
    private int failApproach;

    @Basic
    @Column(name = "SUCCESS_APPROACH", length = 150)
    private int successApproach;

    @Basic
    @Column(name = "DEFAULT_GROUP_ID", length = 150)
    private Long defaultGroupId;

    public ConfigurationEntity() {
        this.id = null;
        this.userId = null;
        this.failApproach = 1;
        this.successApproach = 3;
        this.defaultGroupId = null;
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

    public int getFailApproach() {
        return failApproach;
    }

    public void setFailApproach(int failApproach) {
        this.failApproach = failApproach;
    }

    public Long getDefaultGroupId() {
        return defaultGroupId;
    }

    public void setDefaultGroupId(Long defaultGroupId) {
        this.defaultGroupId = defaultGroupId;
    }

    public int getSuccessApproach() {
        return successApproach;
    }

    public void setSuccessApproach(int succsesApproach) {
        this.successApproach = succsesApproach;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationEntity that = (ConfigurationEntity) o;
        return failApproach == that.failApproach &&
                Objects.equals(defaultGroupId, that.defaultGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(failApproach, defaultGroupId);
    }
}
