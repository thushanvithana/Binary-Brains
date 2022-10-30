package com.example.artzoneapp;

import android.os.Parcel;
import android.os.Parcelable;

public class ArtDetails {

    private String AID;
    private String art_title;
    private String art_description;
    private String image_name;

    public ArtDetails(){

    }

    public ArtDetails(String AID, String art_title, String art_description, String image_name) {
        this.AID = AID;
        this.art_title = art_title;
        this.art_description = art_description;
        this.image_name = image_name;
    }

    public String getAID() {
        return AID;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }

    public String getArt_title() {
        return art_title;
    }

    public void setArt_title(String art_title) {
        this.art_title = art_title;
    }

    public String getArt_description() {
        return art_description;
    }

    public void setArt_description(String art_description) {
        this.art_description = art_description;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
