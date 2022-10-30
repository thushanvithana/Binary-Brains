package com.example.artzoneapp;

public class ItemRow {


    private String id;
    private String name;
    private String image;
    private String useruid;

    public ItemRow(){

    }

    public ItemRow(String id, String name, String image, String useruid) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.useruid = useruid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
    }
}

//    //getView().findViewById();
