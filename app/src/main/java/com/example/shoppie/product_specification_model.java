package com.example.shoppie;

public class product_specification_model {
    public static final int SPECIFICATION_TITLE=0;
    public static final int SPECIFICATION_BODY=1;
    private int type;

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    ///////////////specification title
    private String title;
    public product_specification_model(String title) {
        this.title = title;
    }



    public product_specification_model(int type, String title) {
        this.type = type;
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    ////////////////////specification body
    private String featureName;
    private String featureVakue;

    public product_specification_model(int type, String featureName, String featureVakue) {
        this.type = type;
        this.featureName = featureName;
        this.featureVakue = featureVakue;
    }
    public String getFeatureName() {
        return featureName;
    }
    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
    public String getFeatureVakue() {
        return featureVakue;
    }
    public void setFeatureVakue(String featureVakue) {
        this.featureVakue = featureVakue;
    }
}
