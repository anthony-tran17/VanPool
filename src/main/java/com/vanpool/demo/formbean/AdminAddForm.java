package com.vanpool.demo.formbean;

import javax.validation.constraints.NotEmpty;

public class AdminAddForm {
    @NotEmpty(message = "Please enter vehicle number")
    private String vehicleNo;
    @NotEmpty(message = "Please enter vehicle type")
    private String vehicleType;
    @NotEmpty(message = "please enter owner name")
    private String owner;
    @NotEmpty(message = "Please provide maximum occupancy of the vehicle")
    private String maxOcc;

    public AdminAddForm(){

    }

    public AdminAddForm(String vehicle_no, String vehicle_type, String vehicle_owner, String max_occ){
        this.vehicleNo = vehicle_no;
        this.vehicleType = vehicle_type;
        this.owner = vehicle_owner;
        this.maxOcc = max_occ;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMaxOcc() {
        return maxOcc;
    }

    public void setMaxOcc(String maxOcc) {
        this.maxOcc = maxOcc;
    }
}
