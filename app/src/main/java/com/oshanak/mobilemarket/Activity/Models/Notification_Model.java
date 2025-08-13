package com.oshanak.mobilemarket.Activity.Models;

public class Notification_Model {
    public String Notification_Id;
    public String Notification_Date;
    public String Notification_Status;

    public Notification_Model() {
    }

    public String getNotification_Id() {
        return Notification_Id;
    }

    public void setNotification_Id(String notification_Id) {
        Notification_Id = notification_Id;
    }

    public String getNotification_Date() {
        return Notification_Date;
    }

    public void setNotification_Date(String notification_Date) {
        Notification_Date = notification_Date;
    }

    public String getNotification_Status() {
        return Notification_Status;
    }

    public void setNotification_Status(String notification_Status) {
        Notification_Status = notification_Status;
    }

    public Notification_Model(String notification_Id, String notification_Date, String notification_Status) {
        Notification_Id = notification_Id;
        Notification_Date = notification_Date;
        Notification_Status = notification_Status;

    }

    @Override
    public String toString() {
        return "Notification_Model{" +
                "Notification_Id='" + Notification_Id + '\'' +
                ", Notification_Date='" + Notification_Date + '\'' +
                ", Notification_Status='" + Notification_Status + '\'' +
                '}';
    }
}
