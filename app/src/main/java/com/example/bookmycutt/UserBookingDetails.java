package com.example.bookmycutt;

public class UserBookingDetails {
    public UserBookingDetails(String UserName, String UserProfile, String UserGender, String Date, String Time,String contact) {
        this.UserName = UserName;
        this.UserProfile = UserProfile;
        this.UserGender = UserGender;
        this.Date = Date;
        this.Time = Time;
        this.Contact=contact;
    }

    public UserBookingDetails() {
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserProfile() {
        return UserProfile;
    }

    public void setUserProfile(String userProfile) {
        UserProfile = userProfile;
    }

    public String getUserGender() {
        return UserGender;
    }

    public void setUserGender(String userGender) {
        UserGender = userGender;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    private String UserName;
    private String UserProfile;
    private String UserGender;
    private String Date;
    private String Time;

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    private String Contact;
}
