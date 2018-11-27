package com.provectus.formula.alexis.models;

public class UserReturn {
    private String name;
    private String email;
    private boolean awsExist;

    public UserReturn() {
    }

    public UserReturn(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAwsExist() {
        return awsExist;
    }

    public void setAwsExist(boolean awsExist) {
        this.awsExist = awsExist;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserReturn user = (UserReturn) o;

        if (awsExist != user.awsExist) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        return email != null ? email.equals(user.email) : user.email == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (awsExist ? 1 : 0);
        return result;
    }


}
