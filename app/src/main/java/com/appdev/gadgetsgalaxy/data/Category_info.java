package com.appdev.gadgetsgalaxy.data;

public class Category_info {

    private String catTitle;
    private String imageUrl;

    public Category_info(String catTitle, String imageUrl, String catDescription) {
        this.catTitle = catTitle;
        this.imageUrl = imageUrl;
        this.catDescription = catDescription;
    }
    public Category_info() {
    }


    private String catDescription;

    public String getCatTitle() {
        return catTitle;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCatDescription() {
        return catDescription;
    }

    public void setCatDescription(String catDescription) {
        this.catDescription = catDescription;
    }
}
