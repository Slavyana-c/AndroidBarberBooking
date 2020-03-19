package com.example.androidbarberbooking.Model;

public class Banner {
    //Lookbook and banner is the same
    private String image;
    public Banner(String image) {
        this.image = image;
    }

    public Banner(){}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
