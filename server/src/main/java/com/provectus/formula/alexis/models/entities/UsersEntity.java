package com.provectus.formula.alexis.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
public class UsersEntity {
    private int id;
    private String email;
    private String password;
    private String name;
    private String awsid;

    public UsersEntity(String email, String password, String name, String awsid) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.awsid = awsid;
    }

    public UsersEntity() {
    }

    public UsersEntity(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "EMAIL", nullable = false, length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "PASSWORD", nullable = false, length = 200)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "NAME", nullable = false, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "AWSID")
    public String getAwsid() {
        return awsid;
    }

    public void setAwsid(String awsid) {
        this.awsid = awsid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersEntity that = (UsersEntity) o;

        if (id != that.id) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (awsid != null ? !awsid.equals(that.awsid) : that.awsid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (awsid != null ? awsid.hashCode() : 0);
        return result;
    }
}
