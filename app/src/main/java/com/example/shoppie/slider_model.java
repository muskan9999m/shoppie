package com.example.shoppie;

public class slider_model {
    private String banner;

private String bgcolor;

    public slider_model(String banner, String bgcolor) {
        this.banner = banner;
        this.bgcolor = bgcolor;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBgcolor() {
       return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
      this.bgcolor = bgcolor;
    }
}
