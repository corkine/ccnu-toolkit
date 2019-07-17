package com.mazhangjing.recorder.bean;

import java.util.ArrayList;
import java.util.List;

public class Item {
    public String name;
    public String value;
    //可选 int、str
    public String castType = "str";
    public Integer sizeSmallerThan;
    public Integer equalSize;
    public List<String> matchIn = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCastType() {
        return castType;
    }

    public void setCastType(String castType) {
        this.castType = castType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSizeSmallerThan() {
        return sizeSmallerThan;
    }

    public void setSizeSmallerThan(Integer sizeSmallerThan) {
        this.sizeSmallerThan = sizeSmallerThan;
    }

    public Integer getEqualSize() {
        return equalSize;
    }

    public void setEqualSize(Integer equalSize) {
        this.equalSize = equalSize;
    }

    public List<String> getMatchIn() {
        return matchIn;
    }

    public void setMatchIn(List<String> matchIn) {
        this.matchIn = matchIn;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", caseType=" + castType +
                ", sizeSmallerThan=" + sizeSmallerThan +
                ", equalSize=" + equalSize +
                ", matchIn=" + matchIn +
                '}';
    }
}
