package com.vanpool.demo.DAO;

import com.vanpool.demo.formbean.LoginForm;
import com.vanpool.demo.formbean.UsersForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;


@Repository("Users")
public class UsersDAO {

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private PasswordEncoder encoder;

    public int create(UsersForm userform){
        int status = 0;
        boolean emailStatus = false;
        int tel_no;
        int pincode;
        String sql = "SELECT * FROM Users";
        SqlRowSet rowset = template.queryForRowSet(sql);
        while (rowset.next()) {
            if (rowset.getString("Email").equals(userform.getEmail())) {
                emailStatus = true;
                break;
            }
        }
        if(emailStatus == false){
                tel_no = Integer.parseInt(userform.getTel_no());
                pincode = Integer.parseInt(userform.getPincode());
                String insertSql = "INSERT INTO Users(First_name,Last_name,Email,Password,Telephone,Line1,Line2,City,State" +
                        ",Pincode,Level) values" +
                        "('" + userform.getFirst_name() + "','" + userform.getLast_name() + "','" + userform.getEmail() + "','"
                        + encoder.encode(userform.getPassword()) + "'," + tel_no + ",'" + userform.getLine1() + "','"
                        + userform.getLine2() + "','" + userform.getCity() +"','" + userform.getState() +"',"
                        + pincode + "," + userform.getLevel() + ")";
                status = template.update(insertSql);
            }

        return status;
    }


    public String[] validateLogin(LoginForm loginForm) {
        boolean result = false;
        String[] resultArray = new String[2];
        String searchSql = "SELECT first_name,Level,Password FROM USERS WHERE Email = '" + loginForm.getEmail() + "'";
        SqlRowSet rowset = template.queryForRowSet(searchSql);
        while (rowset.next()) {
            if ((rowset.getInt("Level")) != 2) {
                result = encoder.matches(loginForm.getPassword(), rowset.getString("Password"));
                if (result == true) {
                    String name = rowset.getString("first_name");
                    resultArray[0] = name;
                    if (rowset.getInt("Level") == 0) {
                        resultArray[1] = "Passenger";
                    } else if (rowset.getInt("Level") == 1) {
                        resultArray[1] = "Driver";
                    }
                } else {
                    resultArray[0] = "invalid";
                    resultArray[1] = "invalid";
                }
            } else {
                resultArray[0] = rowset.getString("first_name");
                resultArray[1] = "Administrator";
            }
        }
        return resultArray;
    }
}
