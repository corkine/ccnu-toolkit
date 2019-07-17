package com.mazhangjing.recorder.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Person {
    public String projectName = "新建项目";
    public String noDataSymbol = "-1";
    public List<Group> groupList = new ArrayList<>();

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public String getNoDataSymbol() {
        return noDataSymbol;
    }

    public void setNoDataSymbol(String noDataSymbol) {
        this.noDataSymbol = noDataSymbol;
    }

    @Override
    public String toString() {
        return super.toString() + "Person[" + groupList.get(0).itemList.get(0).value + "]";
        /*return "Person{" +
                "projectName='" + projectName + '\'' +
                ", noDataSymbol='" + noDataSymbol + '\'' +
                ", groupList=" + groupList +
                '}';*/
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(projectName, person.projectName) &&
                Objects.equals(noDataSymbol, person.noDataSymbol) &&
                Objects.equals(groupList, person.groupList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectName, noDataSymbol, groupList);
    }
}
