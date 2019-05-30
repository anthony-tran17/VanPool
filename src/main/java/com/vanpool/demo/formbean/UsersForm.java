package com.vanpool.demo.formbean;

import javax.validation.constraints.*;

public class UsersForm {

    @NotEmpty(message = "Please fill out the first name")
    private String first_name;
    @NotEmpty(message = "Please fill out the last name")
    private String last_name;
    @NotEmpty(message = "Please provide your email id")
    private String email;
    @NotEmpty(message = "Password field cannot be blank")
    private String password;
    @NotEmpty(message = "Please fill out the telephone number")
    private String tel_no;
    @NotEmpty(message = "Please enter your address")
    private String line1;
    @NotEmpty(message = "Please enter your address")
    private String line2;
    @NotEmpty(message = "Please enter your city")
    private String city;
    @NotEmpty(message = "Please enter your state")
    private String state;
    @NotEmpty(message = "Please provide your pincode")
    @Size(min = 5, max = 6)
    private String pincode;
    @NotNull
    private int level;

    public UsersForm(){

    }

    public UsersForm(String fname, String lname, String email, String password, String tno, String line1, String line2
            , String city, String state, String pincode, int level)
    {
        this.first_name = fname;
        this.last_name = lname;
        this.email = email;
        this.password = password;
        this.tel_no = tno;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.level = level;

    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        System.out.println("line2:" + line2);
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
