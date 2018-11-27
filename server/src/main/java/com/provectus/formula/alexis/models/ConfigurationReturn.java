package com.provectus.formula.alexis.models;

public class ConfigurationReturn {
    private int failApproach, successApproach;
    private Long defaultGroupId;

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

    public void setSuccessApproach(int successApproach) {
        this.successApproach = successApproach;
    }
}
