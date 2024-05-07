package com.appdev.gadgetsgalaxy.data;

public class Admin_panel_data {
    private int count;
    private String title;

    public Admin_panel_data(int count, String title) {
        this.count = count;
        this.title = title;
    }
    public Admin_panel_data(){}
    public int getCount() {
        return count;
    }

    public String getTitle() {
        return title;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
