package com.mazhangjing.recorder.bean;

import java.util.ArrayList;
import java.util.List;

public class Group {
    public String groupName;
    public List<Item> itemList = new ArrayList<>();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupName='" + groupName + '\'' +
                ", itemList=" + itemList +
                '}';
    }
}
