package com.example.shoppie;

import java.util.List;

public class home_page_model {

    //public static final int BANNER_SLIDER=0;
    public static final int STRIP_AD_BANNER=1;
    public static final int HORIZONTAL_PRODUCT_VIEW=2;
    public static final int GRID_PRODUCT_VIEW=3;

    private int type;
    private String bgcolor;
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    /////////////banner slider/////////////
  /*  private List<slider_model> sliderModelList;

    public home_page_model(int type, List<slider_model> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }


    public List<slider_model> getSliderModelList() {
        return sliderModelList;
    }
    public void setSliderModelList(List<slider_model> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }*/
    /////////////banner slider/////////////

    //////////////STRIP AD////////////////
    private String resource;


    public home_page_model(int type, String resource, String bgcolor) {
        this.type = type;
        this.resource = resource;
        this.bgcolor = bgcolor;
    }

    public String getResource() {
        return resource;
    }
    public void setResource(String  resource) {
        this.resource = resource;
    }
    public String getBgcolor() {
        return bgcolor;
    }
    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }



    private String title;
    List<horizontal_product_model>horizontalProductModelList;

    //////////////HORIZONTAL PRODUCT VIEW

    private List<wishList_model> viewallList;
    public List<wishList_model> getViewallList() {
        return viewallList;
    }

    public void setViewallList(List<wishList_model> viewallList) {
        this.viewallList = viewallList;
    }

    public home_page_model(int type, String title, String bgcolor, List<horizontal_product_model> horizontalProductModelList, List<wishList_model> viewallList) {
        this.type = type;
        this.title = title;
        this.viewallList=viewallList;
        this.horizontalProductModelList = horizontalProductModelList;
        this.bgcolor=bgcolor;
    }
/////////////////grid
    public home_page_model(int type, String title,String bgcolor, List<horizontal_product_model> horizontalProductModelList) {
        this.type = type;
        this.title = title;
        this.bgcolor=bgcolor;
        this.horizontalProductModelList = horizontalProductModelList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<horizontal_product_model> getHorizontalProductModelList() {
        return horizontalProductModelList;
    }

    public void setHorizontalProductModelList(List<horizontal_product_model> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
    }
}