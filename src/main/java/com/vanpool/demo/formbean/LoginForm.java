package com.vanpool.demo.formbean;

import javax.validation.constraints.NotEmpty;

public class LoginForm {

    @NotEmpty(message = "Please enter your email ID")
    private String email;
    @NotEmpty(message = "Please enter your password")
    private String password;

    public LoginForm(){

    }

    public LoginForm(String emailId, String password){
        this.email = emailId;
        this.password = password;
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
}
