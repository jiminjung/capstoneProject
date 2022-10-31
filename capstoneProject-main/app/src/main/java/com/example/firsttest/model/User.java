package com.example.firsttest.model;

public class User {
    String userAge;
    String userName;
    String userPhoneNumber;
    String userIP;

    public User(String userName, String userAge, String userPhoneNumber, String userIP) {
        this.userAge = userAge;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userIP = userIP;
    }

    public String getUserAge() { return userAge; }

    public String getUserName() {
        return userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String getUserIP() {
        return userIP;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhoneNumber(String userPhoneNumber) { this.userPhoneNumber = userPhoneNumber; }

    public void setUserIP(String userIP) {
        this.userIP = userIP;
    }

}


