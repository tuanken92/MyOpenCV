package com.example.myopencv.model;

import java.io.Serializable;

public class UserData implements Serializable {
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getTableid() {
        return tableid;
    }

    public void setTableid(int tableid) {
        this.tableid = tableid;
    }

    public UserData(String company, String name, String position, String role, int tableid) {
        this.company = company;
        this.name = name;
        this.position = position;
        this.role = role;
        this.tableid = tableid;
    }

    public UserData() {
        this.company = "THH";
        this.name = "THH";
        this.position = "THH";
        this.role = "THH";
        this.tableid = 0;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "company='" + company + '\'' +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", role='" + role + '\'' +
                ", tableid=" + tableid +
                '}';
    }

    private String company;
    private String name;
    private String position;
    private String role;
    private int tableid;
}
