package com.vanpool.demo.DAO;

import com.vanpool.demo.formbean.AdminAddForm;
import com.vanpool.demo.formbean.StopsForm;
import com.vanpool.demo.formbean.VehicleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AdminDAO {
    @Autowired
    private JdbcTemplate template;

    public List<VehicleForm> getVehicleList() {
        List<VehicleForm> vehicleList = new ArrayList<VehicleForm>();

        String vehicleSql = "SELECT a.VehicleNo,a.VehicleType,b.First_name,b.Email,a.MaxOcc FROM SEProject.Vehicles a," +
                "SEProject.Users b WHERE a.UserId = b.UserId";
        SqlRowSet rowset = template.queryForRowSet(vehicleSql);
        while (rowset.next()) {
            VehicleForm vehicle_form = new VehicleForm();
            vehicle_form.setVehicleNo(rowset.getString("VehicleNo"));
            vehicle_form.setVehicleType(rowset.getString("VehicleType"));
            vehicle_form.setFirst_name(rowset.getString("First_name"));
            vehicle_form.setEmail(rowset.getString("Email"));
            vehicle_form.setMaxOcc(Integer.toString(rowset.getInt("MaxOcc")));
            vehicleList.add(vehicle_form);
        }
        return vehicleList;
    }

    public int createVehicle(AdminAddForm adminAddForm) {
        int vehiclePresent = 0;
        int status = 0;
        int available = 0;
        int userId = 0;
        String vehicleSql = "SELECT * FROM Vehicles";
        SqlRowSet rowset = template.queryForRowSet(vehicleSql);
        while (rowset.next()) {
            if (adminAddForm.getVehicleNo().equals(rowset.getString("VehicleNo"))) {
                vehiclePresent = 1;
                break;
            }
        }
        if (vehiclePresent == 0) {
            String userSql = "SELECT UserId FROM SEProject.Users WHERE Email = '" + adminAddForm.getOwner() + "'";
            SqlRowSet user = template.queryForRowSet(userSql);
            if(user.next()){
                userId = user.getInt("UserId");
            }
            else{
                status = 0;
            }
            String vehicleInsertSql = "INSERT INTO SEProject.Vehicles(VehicleNo,VehicleType,UserId,Available,maxOcc)" +
                    "VALUES('" + adminAddForm.getVehicleNo() + "','" + adminAddForm.getVehicleType() + "'," + userId + "," +
                    available + "," + Integer.parseInt(adminAddForm.getMaxOcc()) + ")";
            status = template.update(vehicleInsertSql);
        }
        return status;
    }

    public List<String> getVehicleNumber(){
        List<String> vehicleNo = new ArrayList<>();
        String availableVechicleSql = "SELECT VehicleNo FROM SEProject.Vehicles WHERE Available = 0";
        SqlRowSet vehicleSet = template.queryForRowSet(availableVechicleSql);
        while (vehicleSet.next()){
            vehicleNo.add(vehicleSet.getString("VehicleNo"));
        }
        return vehicleNo;
    }

    public int createStops(StopsForm stopsForm) {
        int stopStatus = 0;
        int status = 0;
        int id = 0;
        String vehicleNumber = "";
        int maxOcc = 0;
        String stopCheckSql = "SELECT FromId, ToId FROM SEProject.Stops WHERE FromStop = '" + stopsForm.getFromPlace() + "' AND ToStop" +
                "= '" + stopsForm.getToPlace() + "'";
        SqlRowSet rowset = template.queryForRowSet(stopCheckSql);
        if (rowset.next())
            stopStatus = 1;
        if (stopStatus == 0) {
            String numberOccupancySql = "SELECT VehicleNo,MaxOcc FROM SEProject.Vehicles WHERE VehicleNo = '" + stopsForm.getVehicleNo()
                    + "'";
            SqlRowSet numberSet = template.queryForRowSet(numberOccupancySql);
            while (numberSet.next()){
                vehicleNumber = numberSet.getString("VehicleNo");
                maxOcc = numberSet.getInt("MaxOcc");
            }
            String idSql = "SELECT max(FromId) AS fromID FROM SEProject.Stops";
            SqlRowSet idSet = template.queryForRowSet(idSql);
            while (idSet.next()) {
                id = idSet.getInt("fromID");
            }
            id = id + 2;
            String insertStopSql = "INSERT INTO SEProject.Stops(ToId,FromStop,ToStop,Cost,VehicleNo,MaxOcc)VALUES(" + id + ",'" +
                    stopsForm.getFromPlace() + "','" + stopsForm.getToPlace() + "'," + Float.parseFloat(stopsForm.getCost())
                    + ",'" + vehicleNumber + "'," + maxOcc + ")";
            status = template.update(insertStopSql);
            if(status == 1){
                String updateAvailable = "UPDATE SEProject.Vehicles SET Available = 1 WHERE VehicleNo = '" + vehicleNumber + "'";
                status = template.update(updateAvailable);
            }
        }
        return status;
    }

        public List<StopsForm> getStopList() {
            List<StopsForm> stopList = new ArrayList<>();
            String stopSql = "SELECT * FROM SEProject.stops";
            SqlRowSet rowset = template.queryForRowSet(stopSql);
            while (rowset.next()){
                StopsForm stopsForm = new StopsForm();
                stopsForm.setFromPlace(rowset.getString("FromStop"));
                stopsForm.setToPlace(rowset.getString("ToStop"));
                stopsForm.setVehicleNo(rowset.getString("VehicleNo"));
                stopsForm.setCost(Float.toString(rowset.getFloat("cost")));
                stopList.add(stopsForm);
            }
            return stopList;
    }

    public List<VehicleForm> getDetailsOfVehicle(String vehicleNo){
        List<VehicleForm> vehicleForm = new ArrayList<>();
        String vehicleDetailSql = "SELECT a.VehicleNo, a.VehicleType, b.First_name, b.Email, a.MaxOcc FROM Vehicles a,Users b " +
                "WHERE a.VehicleNo = '" + vehicleNo +"' AND a.UserId = b.UserId";
        SqlRowSet rowset = template.queryForRowSet(vehicleDetailSql);
        while(rowset.next()){
            VehicleForm vehicle_form = new VehicleForm();
            vehicle_form.setVehicleNo(vehicleNo);
            vehicle_form.setVehicleType(rowset.getString("VehicleType"));
            vehicle_form.setFirst_name(rowset.getString("First_name"));
            vehicle_form.setEmail(rowset.getString("Email"));
            vehicle_form.setMaxOcc(Integer.toString(rowset.getInt("maxOcc")));
            vehicleForm.add(vehicle_form);
        }
        return vehicleForm;
    }

    public int updateVehicle(VehicleForm vehicleForm){
        int status = 0;
        int userId = 0;
        String userSql = "SELECT UserId FROM SEProject.Users WHERE Email = '" + vehicleForm.getEmail() + "'";
        SqlRowSet userSet = template.queryForRowSet(userSql);
        while (userSet.next()){
            userId = userSet.getInt("UserId");
        }
        String updateSql = "UPDATE SEProject.Vehicles SET VehicleType = '" + vehicleForm.getVehicleType() + "', UserId = " +
                userId + ", MaxOcc = " + Integer.parseInt(vehicleForm.getMaxOcc()) + " WHERE VehicleNo = '"
                +vehicleForm.getVehicleNo() + "'";
        status = template.update(updateSql);
        return status;
    }

    public List<StopsForm> getRouteDetails(String vehicleNo){
        List<StopsForm> stopsForm = new ArrayList<>();
        String getRouteDetails = "SELECT FromStop, ToStop, Cost, VehicleNo FROM SEProject.Stops WHERE VehicleNo = '"+
                vehicleNo + "'";
        SqlRowSet rowSet = template.queryForRowSet(getRouteDetails);
        while(rowSet.next()){
            StopsForm stopsForm1 = new StopsForm();
            stopsForm1.setFromPlace(rowSet.getString("FromStop"));
            stopsForm1.setToPlace(rowSet.getString("ToStop"));
            stopsForm1.setCost(Float.toString(rowSet.getFloat("Cost")));
            stopsForm1.setVehicleNo(rowSet.getString("VehicleNo"));
            stopsForm.add(stopsForm1);
        }
        return stopsForm;
    }

    public int updateRoute(StopsForm stopsForm, String vehicleNo){
        int status = 0;
        int fromId = 0, toId = 0;
        Float cost = Float.parseFloat(stopsForm.getCost());
        String vehicleNumber = stopsForm.getVehicleNo();
        String fromIdAndToId = "SELECT FromId, ToId FROM SEProject.stops WHERE FromStop = '" + stopsForm.getFromPlace() +
                "' AND ToStop = '" + stopsForm.getToPlace() + "'";
        SqlRowSet rowset = template.queryForRowSet(fromIdAndToId);
        while (rowset.next()){
            fromId = rowset.getInt("FromId");
            toId = rowset.getInt("ToId");
        }
        String updateRouteSql = "UPDATE SEProject.Stops SET Cost = " + cost + ", VehicleNo = '"
                + vehicleNumber + "' WHERE FromId = " + fromId + " AND ToId = " + toId +"";
        status = template.update(updateRouteSql);
        if(status == 1 && vehicleNo!= vehicleNumber) {
            String updateAvailability = "UPDATE SEProject.Vehicles SET Available = 1 WHERE VehicleNo = '" + stopsForm.getVehicleNo()
                    + "'";
            status = template.update(updateAvailability);
        }
        if(status == 1 && vehicleNo!= vehicleNumber){
            String updatePreviousVehicle = "UPDATE SEProject.Vehicles SET Available = 0 WHERE VehicleNo = '" +
                    vehicleNo + "'";
            status = template.update(updatePreviousVehicle);
        }
        return status;
    }
}

