package com.example.shoppie;

public class categorymodel {
    String categoryLink;
    String categoryName;

    public categorymodel(String categoryLink, String categoryName) {
        this.categoryLink = categoryLink;
        this.categoryName = categoryName;
    }

    public String getCategoryLink() {
        return categoryLink;
    }

    public void setCategoryLink(String categoryLink) {
        this.categoryLink = categoryLink;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
