package com.romero.parcial1v5snrm;

public class Contacts {
    private String name;
    private String phone;
    private int photo;
    private boolean fav;

    public Contacts(String name, String phone, int photo, boolean fav) {
        this.name = name;
        this.phone = phone;
        this.photo = photo;
        this.fav = fav;
    }
    public Contacts(){}

    //getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }
}
