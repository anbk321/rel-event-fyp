package com.example.fyptrial;

public class UserModel {

    private String userName;
    private String userID;
    private String city;

    public UserModel() {
    }

    public UserModel(String userName, String userID, String city) {
        this.userName = userName;
        this.userID = userID;
        this.city = city;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String email) {
        this.userID = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

//    @Override
//    public String toString() {
//        return "UserModel{" +
//                "userName='" + userName + '\'' +
//                ", email='" + email + '\'' +
//                ", phoneNo='" + phoneNo + '\'' +
//                '}';
//    }
}
