package com.example.shoppie;

import java.util.Date;

public class reward_model {
    private String type;
    private String lowerlimit, upperlimit;
    private String discoramount;
    private boolean alreadyused;
    private String couponId;

    public boolean isAlreadyused() {
        return alreadyused;
    }

    public void setAlreadyused(boolean alreadyused) {
        this.alreadyused = alreadyused;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public reward_model(String couponId, String type, String lowerlimit, String upperlimit, String discoramount, Date timestamp, String couponbody, boolean alreadyused) {
        this.type = type;
        this.lowerlimit = lowerlimit;
        this.couponId=couponId;
        this.upperlimit = upperlimit;
        this.discoramount = discoramount;
        this.timestamp = timestamp;
        this.couponbody = couponbody;
        this.alreadyused=alreadyused;
    }

    private Date timestamp;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLowerlimit() {
        return lowerlimit;
    }

    public void setLowerlimit(String lowerlimit) {
        this.lowerlimit = lowerlimit;
    }

    public String getUpperlimit() {
        return upperlimit;
    }

    public void setUpperlimit(String upperlimit) {
        this.upperlimit = upperlimit;
    }

    public String getDiscoramount() {
        return discoramount;
    }

    public void setDiscoramount(String discount) {
        this.discoramount = discount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCouponbody() {
        return couponbody;
    }

    public void setCouponbody(String couponbody) {
        this.couponbody = couponbody;
    }

    private String couponbody;

}
