package com.example.shoppie;

public class addresses_model {
    private String fullname;
    private String pincode;
    private String address;
    private String mobileno;
    private Boolean SELECTED;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getSELECTED() {
        return SELECTED;
    }

    public void setSELECTED(Boolean SELECTED) {
        this.SELECTED = SELECTED;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public addresses_model(String fullname, String pincode, String address, Boolean SELECTED, String mobileno) {
        this.fullname = fullname;
        this.SELECTED=SELECTED;
        this.pincode = pincode;
        this.mobileno=mobileno;
        this.address = address;
    }
}
