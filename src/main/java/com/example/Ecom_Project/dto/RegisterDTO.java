package com.example.Ecom_Project.dto;

public class RegisterDTO {

    private String userName;
    private String userEmail;
    private long userPhoneNo;
    private String userPassword;
    private String userGender;
    private String userAddress;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public long getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(long userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public RegisterDTO(String userName, String userEmail, long userPhoneNo, String userPassword, String userGender, String userAddress) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhoneNo = userPhoneNo;
        this.userPassword = userPassword;
        this.userGender = userGender;
        this.userAddress = userAddress;
    }

    public RegisterDTO() {
    }
}
