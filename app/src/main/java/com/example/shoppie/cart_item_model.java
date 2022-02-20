package com.example.shoppie;

import java.util.ArrayList;
import java.util.List;

public class cart_item_model {
    public static final int CART_ITEM = 0;
    public static final int TOTAL_AMOUNT = 1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /////////cart item
    private boolean qtyerror;
    private List<String> qtyIDS;
    private String productID;
    private String productImage;
    private long freeCoupons;
    private long productQuantity;
    private long offersApplied;
    private long couponsApplied;
    private long maxqty;
    private long stockqty;
    private boolean instock;
    private String discountedpric;

    public String getDiscountedpric() {
        return discountedpric;
    }

    public void setDiscountedpric(String discountedpric) {
        this.discountedpric = discountedpric;
    }

    private String productTitle;
    private String productPrcice;
    private String cuttedPrice;
    private String selectedCouponId;

    public String getSelectedCouponId() {
        return selectedCouponId;
    }

    public void setSelectedCouponId(String selectedCouponId) {
        this.selectedCouponId = selectedCouponId;
    }

    public boolean isInstock() {
        return instock;
    }

    public void setInstock(boolean instock) {
        this.instock = instock;
    }

    public long getMaxqty() {
        return maxqty;
    }

    public void setMaxqty(long maxqty) {
        this.maxqty = maxqty;
    }

    public List<String> getQtyIDS() {
        return qtyIDS;
    }

    public void setQtyIDS(List<String> qtyIDS) {
        this.qtyIDS = qtyIDS;
    }

    public long getStockqty() {
        return stockqty;
    }

    public void setStockqty(long stockqty) {
        this.stockqty = stockqty;
    }

    public boolean isQtyerror() {
        return qtyerror;
    }

    public void setQtyerror(boolean qtyerror) {
        this.qtyerror = qtyerror;
    }

    public cart_item_model(int type, String productID, String productImage, long freeCoupons, long productQuantity, long offersApplied, long couponsApplied, String productTitle, String productPrcice, String cuttedPrice, boolean instock, long maxqty, long stockqty) {
        this.type = type;
        this.productImage = productImage;
        this.productID = productID;
        this.stockqty=stockqty;
        this.freeCoupons = freeCoupons;
        this.productQuantity = productQuantity;
        this.instock=instock;
        this.offersApplied = offersApplied;
        this.maxqty=maxqty;
        qtyerror=false;
        this.couponsApplied = couponsApplied;
        this.productTitle = productTitle;
        this.productPrcice = productPrcice;
        qtyIDS=new ArrayList<>();
        this.cuttedPrice = cuttedPrice;
    }


    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public long getFreeCoupons() {
        return freeCoupons;
    }

    public void setFreeCoupons(long freeCoupons) {
        this.freeCoupons = freeCoupons;
    }

    public long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public long getOffersApplied() {
        return offersApplied;
    }

    public void setOffersApplied(long offersApplied) {
        this.offersApplied = offersApplied;
    }

    public long getCouponsApplied() {
        return couponsApplied;
    }

    public void setCouponsApplied(long couponsApplied) {
        this.couponsApplied = couponsApplied;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductPrcice() {
        return productPrcice;
    }

    public void setProductPrcice(String productPrcice) {
        this.productPrcice = productPrcice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }
    //////////cart item

    /////////cart total
    private String deliveryPrice;

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    private int totalItems,totalItemPrice,savedAmount,totalAmount;

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(int totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public cart_item_model(int type)
    {
        this.type=type;
    }
}
