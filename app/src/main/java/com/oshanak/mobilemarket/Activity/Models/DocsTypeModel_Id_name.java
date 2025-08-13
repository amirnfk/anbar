package com.oshanak.mobilemarket.Activity.Models;

public class DocsTypeModel_Id_name {
    int ID;
    String Title;

    public DocsTypeModel_Id_name(int ID, String title) {
        this.ID = ID;
        Title = title;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    @Override
    public String toString() {
        return ""+ Title  ;
    }
}
