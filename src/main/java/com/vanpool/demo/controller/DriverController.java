package com.vanpool.demo.controller;

import com.vanpool.demo.DAO.VehicleDAO;
import com.vanpool.demo.formbean.VehicleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class DriverController {
    @Autowired
    UsersController usersController;
    @Autowired
    VehicleDAO vehicleDAO;

    @RequestMapping(value="/driverHome", method = RequestMethod.GET)
    public String getDriverHome(Model model){
        String userName = usersController.loginArray[0];
        model.addAttribute("userName", userName);
        return "DriverHomePage";
    }

    @RequestMapping(value="/addVehicleDriver", method = RequestMethod.GET)
    public String addVehiclePage(Model model){
        VehicleForm vehicle_form = new VehicleForm();
        model.addAttribute("userName", usersController.loginArray[0]);
        model.addAttribute("level",usersController.loginArray[1]);
        model.addAttribute("vehicle_form",vehicle_form);
        return "AddVehicleDriver";
    }

    @RequestMapping(value= "/addVehicleDriver", method = RequestMethod.POST)
    public String addVehicle(Model model, @ModelAttribute("vehicle_form") @Valid VehicleForm vehicle_form, BindingResult result,
                             final RedirectAttributes redirectAttributes){
        int status;
        if(result.hasErrors()){
            return "addVehicleDriver";
        }
        try{
            status =  vehicleDAO.createVehicle(vehicle_form, usersController.email);
            if(status == 1){
                model.addAttribute("errorMessage","Your vehicle is added!!!");
                return "DriverHomePage";
            }
            else
            {
                model.addAttribute("errorMessage","Sorry, vehicle Id already exists");
                return "addVehicleDriver";
            }
        }
        catch(Exception e){
            model.addAttribute("errorMessage", "Error:" + e.getMessage());
            return "addVehicleDriver";
        }


    }

}

