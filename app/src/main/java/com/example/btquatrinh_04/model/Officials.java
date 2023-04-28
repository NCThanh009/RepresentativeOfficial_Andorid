package com.example.btquatrinh_04.model;

public class Officials {
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String channel;

    public Officials(String name, String address, String phone, String email, String website, String channel) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
