package com.example.lab3contentprovider;

import android.graphics.Bitmap;

public class Item {
    private String name;
    private String description;
    private int icon;
    private Bitmap icon2;

    public Bitmap getIcon2() {
        return icon2;
    }

    public void setIcon2(Bitmap icon2) {
        this.icon2 = icon2;
    }

    public Item() {
    }

    public Item(String name, String description, int icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
