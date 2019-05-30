package com.vanpool.demo.DAO;

import com.vanpool.demo.formbean.AdminAddForm;
import com.vanpool.demo.formbean.VehicleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository("Vehicles")
public class VehicleDAO {
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate template;


    public int createVehicle(VehicleForm vehicleForm, String userName){
        int vehicleStatus = 0;
        int status = 0;
        int userId = 0;
        int available = 0;
        String sql = "SELECT * FROM Vehicles";
        SqlRowSet rowset = template.queryForRowSet(sql);
        while(rowset.next()){
            if(vehicleForm.getVehicleNo().equals(rowset.getString("VehicleNo"))){
                vehicleStatus = 1;
                break;
            }
        }
        if(vehicleStatus == 0) {
            if (userName == "Admin@gmail.com") {
                AdminAddForm adminAddForm = new AdminAddForm();
                String vehicleInsertSql = "INSERT INTO SEProject.Vehicles(VehicleNo,VehicleType,UserId,Available,maxOcc)" +
                        "VALUES('" + adminAddForm.getVehicleNo() + "','" + adminAddForm.getVehicleType() + "'," + adminAddForm.getOwner() + "," +
                        available + "," + Integer.parseInt(vehicleForm.getMaxOcc()) + ")";
                status = template.update(vehicleInsertSql);

            } else {
                String userSql = "SELECT UserId FROM Users WHERE Email = '" + userName + "'";
                SqlRowSet emailRowSet = template.queryForRowSet(userSql);
                while (emailRowSet.next()) {
                    userId = emailRowSet.getInt("UserId");
                }
                String insertSql = "INSERT INTO SEProject.Vehicles(VehicleNo,VehicleType,UserId,Available,maxOcc)" +
                        "VALUES('" + vehicleForm.getVehicleNo() + "','" + vehicleForm.getVehicleType() + "'," + userId + "," +
                        available + "," + Integer.parseInt(vehicleForm.getMaxOcc()) + ")";
                status = template.update(insertSql);
            }

        }

        return status;
    }
}
