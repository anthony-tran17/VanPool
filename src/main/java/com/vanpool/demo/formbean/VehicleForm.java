package com.vanpool.demo.formbean;

import javax.validation.constraints.NotEmpty;

public class VehicleForm {

    @NotEmpty(message = "Please enter vehicle's license plate number")
    private String vehicleNo;
    @NotEmpty(message = "please enter vehicle's type")
    private String vehicleType;
    @NotEmpty(message = "Please enter maximum occupancy of the vehicle")
    private String maxOcc;

    private String first_name;
    private String email;

    public VehicleForm(){

    }

    public VehicleForm(String vNo, String vType, String maxOcc){
        this.vehicleNo = vNo;
        this.vehicleType = vType;
        this.maxOcc = maxOcc;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }


    public String getMaxOcc() {
        return maxOcc;
    }

    public void setMaxOcc(String maxOcc) {
        this.maxOcc = maxOcc;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
