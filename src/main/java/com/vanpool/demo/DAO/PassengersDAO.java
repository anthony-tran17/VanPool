package com.vanpool.demo.DAO;

import com.vanpool.demo.formbean.RidesForm;
import com.vanpool.demo.formbean.StopsForm;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class PassengersDAO{
    @Autowired
    private JdbcTemplate template;
    public List<RidesForm> getRideList(String username){
        int userID = 0;
        String first_name = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String userSql = "Select UserId, First_name from Users where Email ='"+ username + "'";
        SqlRowSet userrow =template.queryForRowSet(userSql);
        while (userrow.next()) {
            userID = userrow.getInt("UserId");
            first_name = userrow.getString("First_name");
        }

        List<RidesForm> rideList = new ArrayList<RidesForm>();
        String ridesSql = "SELECT * from Rides where Passenger = " + userID + "";
        SqlRowSet rowset = template.queryForRowSet(ridesSql);
        while(rowset.next()){
            Date date = rowset.getDate("RideDate");

            RidesForm rides_form = new RidesForm();
            rides_form.setFromStop(rowset.getString("FromStop"));
            rides_form.setToStop(rowset.getString("ToStop"));
            rides_form.setRideDate(dateFormat.format(date));
            rides_form.setPassengerNo(first_name);
            rides_form.setVehicleNo(rowset.getString("VehicleNo"));
            rides_form.setCost(Float.toString(rowset.getFloat("Amount")));
            rideList.add(rides_form);
        }
        return rideList;
    }
    
    public List <String> getFromStopNames(){
        List<String> fromStopNameList = new ArrayList<String>();
        String fromNameSql = "Select FromStop FROM STOPS";
        SqlRowSet nameset = template.queryForRowSet(fromNameSql);
        while (nameset.next()){
            fromStopNameList.add(nameset.getString("FromStop"));
        }

        return fromStopNameList;

    }

    public List <String> getToStopNames(){
        List<String> toStopNameList = new ArrayList<String>();
        String toNameSql = "Select ToStop FROM STOPS";
        SqlRowSet toNameSet = template.queryForRowSet(toNameSql);
        while (toNameSet.next()){
            toStopNameList.add(toNameSet.getString("ToStop"));
        }

        return toStopNameList;

    }

    public List <Integer> getStopIds(){
        List<Integer> stopList = new ArrayList<Integer>();
        String idSql = "Select FromId FROM STOPS";
        SqlRowSet idset = template.queryForRowSet(idSql);
        while (idset.next()){
            stopList.add(idset.getInt("FromId"));
        }

        return stopList;

    }
     public float getcost(int fromID, int toId){
        String costSql = "Select Cost FROM STOPS where FromId = " + fromID + " and ToId = " + toId;
        SqlRowSet ride = template.queryForRowSet(costSql);
        float costresult = ride.getFloat("Cost");
        return costresult;

    }

    public List<StopsForm> checkForRide(String fromStop, String toStop, String passengerCount){
        List<StopsForm> rideList = new ArrayList<>();
        String vehicleNo;
        String cost;
        int countOfPassenger = Integer.parseInt(passengerCount);
        String rideSql = "SELECT VehicleNo,Cost,MAxOcc FROM SEProject.Stops WHERE Fromstop = '" + fromStop + "'AND Tostop = '" +
                toStop + "'";
        SqlRowSet rideSet = template.queryForRowSet(rideSql);
        if(rideSet.next()){
            vehicleNo = rideSet.getString("VehicleNo");
            cost = Float.toString(rideSet.getFloat("cost"));
            String availableSql = "SELECT MaxOcc FROM SEProject.Stops WHERE VehicleNo = '" + vehicleNo + "' AND " + (rideSet
                    .getInt("MaxOcc") - countOfPassenger) + " > 0";
            SqlRowSet availableSet = template.queryForRowSet(availableSql);
            while (availableSet.next()){
                if(availableSet.getInt("MaxOcc") >= 1){
                    StopsForm stopsForm = new StopsForm();
                    stopsForm.setFromPlace(fromStop);
                    stopsForm.setToPlace(toStop);
                    stopsForm.setVehicleNo(vehicleNo);
                    stopsForm.setCost(cost);
                    stopsForm.setCountOfPassengers(passengerCount);
                    rideList.add(stopsForm);

                }
            }
        }
        else{
            StopsForm stops_form = new StopsForm();
            vehicleNo = "NULL";
            cost = "NULL";
            stops_form.setFromPlace(fromStop);
            stops_form.setToPlace(toStop);
            stops_form.setVehicleNo(vehicleNo);
            stops_form.setCost(cost);
            rideList.add(stops_form);

        }
        return rideList;

    }

    public int insertRides(String vehicleNo, String passengerCount,StopsForm stopsForm, String userName){
        int status = 0;
        List<String> rideList = new ArrayList<>();
        Timestamp date = new Timestamp(new Date().getTime());
       String maxOccSql = "SELECT MaxOcc,Cost FROM SEProject.Stops WHERE VehicleNo = '" + vehicleNo + "'";
       SqlRowSet maxOccSet = template.queryForRowSet(maxOccSql);
       while ( maxOccSet.next()){
           Float cost = maxOccSet.getFloat("Cost");
           String changeMaxOCc = "UPDATE SEProject.Stops SET MaxOcc = " + ((maxOccSet.getInt("MaxOcc") - (Integer.parseInt(passengerCount))))
                   + " WHERE VehicleNo = '" + vehicleNo +"'";
           status = template.update(changeMaxOCc);
           if(status==1){
                String insertRide = "INSERT INTO SEPRoject.Rides(FromStop,ToStop,RideDate,Passenger,VehicleNo,Amount)VALUES" +
                        "('" + stopsForm.getFromPlace() + "','" + stopsForm.getToPlace() + "','" + date + "',( SELECT UserId FROM SEProject.Users" +
                        " WHERE Email = '" + userName + "'), '" + vehicleNo + "'," + cost + ")";
                status = template.update(insertRide);
           }
       }
       return  status;
    }


}
