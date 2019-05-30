package com.vanpool.demo.formbean;

import javax.validation.constraints.NotEmpty;
import java.sql.Time;
import java.util.Date;

public class RidesForm {

    @NotEmpty
    private String fromStop;
    @NotEmpty
    private String toStop;
    @NotEmpty
    private String rideDate;
    @NotEmpty
    private String vehicleNo;
    @NotEmpty
    private String passengerNo;
    @NotEmpty
    private String cost;




    public RidesForm(){

    }


    public String getFromStop() {
        return fromStop;
    }

    public void setFromStop(String fromStop) {
        this.fromStop = fromStop;
    }

    public String getToStop() {
        return toStop;
    }

    public void setToStop(String toStop) {
        this.toStop = toStop;
    }


    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public String getPassengerNo() {
        return passengerNo;
    }

    public void setPassengerNo(String passengerNo) {
        this.passengerNo = passengerNo;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
