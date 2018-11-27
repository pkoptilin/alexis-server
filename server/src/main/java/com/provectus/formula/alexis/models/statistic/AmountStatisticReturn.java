package com.provectus.formula.alexis.models.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AmountStatisticReturn {
    @JsonIgnore
    private String status;
    private int inprogress;
    private int learned;

    public AmountStatisticReturn(String status) {
        this.status = status;
    }

    public AmountStatisticReturn(String status, int inprogress, int learned) {
        this.status = status;
        this.inprogress = inprogress;
        this.learned = learned;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getInprogress() {
        return inprogress;
    }

    public void setInprogress(int inprogress) {
        this.inprogress = inprogress;
    }

    public int getLearned() {
        return learned;
    }

    public void setLearned(int learned) {
        this.learned = learned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AmountStatisticReturn that = (AmountStatisticReturn) o;

        if (inprogress != that.inprogress) return false;
        if (learned != that.learned) return false;
        return status != null ? status.equals(that.status) : that.status == null;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + inprogress;
        result = 31 * result + learned;
        return result;
    }

    @Override
    public String toString() {
        return "AmountStatisticReturn{" +
                "status='" + status + '\'' +
                ", inprogress=" + inprogress +
                ", learned=" + learned +
                '}';
    }
}
