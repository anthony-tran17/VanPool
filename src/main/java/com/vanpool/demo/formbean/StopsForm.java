package com.vanpool.demo.formbean;

import javax.validation.constraints.NotEmpty;

public class StopsForm {
    @NotEmpty(message="Please enter a From Place")
    private String fromPlace;
    @NotEmpty(message="Please enter a To place")
    private String toPlace;
    @NotEmpty(message="Please enter cost for the ride from source to destination")
    private String cost;

    private String vehicleNo;

    private String countOfPassengers;


    public StopsForm(){}

    public StopsForm(String from_place, String to_place, String price, String vehicleNumber, String passengerCount){
        this.fromPlace = from_place;
        this.toPlace = to_place;
        this.cost = price;
        this.vehicleNo = vehicleNumber;
        this.countOfPassengers = passengerCount;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getCountOfPassengers() {
        return countOfPassengers;
    }

    public void setCountOfPassengers(String countOfPassengers) {
        this.countOfPassengers = countOfPassengers;
    }
}
