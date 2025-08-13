package com.oshanak.mobilemarket.Activity.Service.Retrofit;

public class ServerConfig {
    String Code;
    String Name;
    String Description;
    String Value;

    public ServerConfig(String code, String name, String description, String value) {
        Code = code;
        Name = name;
        Description = description;
        Value = value;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "Code='" + Code + '\'' +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", Value='" + Value + '\'' +
                '}';
    }
}
