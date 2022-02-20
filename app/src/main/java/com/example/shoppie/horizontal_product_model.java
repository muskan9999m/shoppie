package com.example.shoppie;

public class horizontal_product_model {
    private String productID;
    private String productImage;
    private String prductTitle;
    private String productDescription;
    private String productPrice;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public horizontal_product_model(String productID, String productImage, String prductTitle, String productDescription, String productPrice) {
        this.productImage = productImage;
        this.prductTitle = prductTitle;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productID=productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getPrductTitle() {
        return prductTitle;
    }

    public void setPrductTitle(String prductTitle) {
        this.prductTitle = prductTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
