package com.vanpool.demo.controller;

import com.vanpool.demo.DAO.AdminDAO;
import com.vanpool.demo.formbean.AdminAddForm;
import com.vanpool.demo.formbean.StopsForm;
import com.vanpool.demo.formbean.VehicleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("userName")
public class AdminController {

    @Autowired
    UsersController usersController;
    @Autowired
    AdminDAO adminDAO;
    @Autowired
    AdminController adminController;

    private String routeVehicleNo;

    @RequestMapping(value="/addDriver", method = RequestMethod.GET)
    public String addDriverPage(Model model){

        String userName = usersController.loginArray[0];
        model.addAttribute("userName", usersController.loginArray[0]);
        return "AddDriver";
    }

    @RequestMapping(value="/adminHome", method = RequestMethod.GET)
    public String getAdminHome(Model model){
        String userName = usersController.loginArray[0];
        model.addAttribute("userName", userName);
        return "AdminHomePage";
    }

    @RequestMapping(value="/manageVehicles", method = RequestMethod.GET)
    public String getVehicleList(Model model){
        String userName = usersController.loginArray[0];
        model.addAttribute("userName",userName);
        List<VehicleForm> vehicleList = new ArrayList<VehicleForm>();
        vehicleList = adminDAO.getVehicleList();
        model.addAttribute("vehicleList",vehicleList);
        return "VehicleList";
    }

    @RequestMapping(value="/adminAddVehicle", method = RequestMethod.GET)
    public String getAddVehicleAdminPage(Model model){
        AdminAddForm adminAdd_form = new AdminAddForm();
        String userName = usersController.loginArray[0];
        model.addAttribute("userName",userName);
        model.addAttribute("adminAdd_form", adminAdd_form);
        return "AddVehicleAdmin";
    }

    @RequestMapping(value="/adminAddVehicle", method = RequestMethod.POST)
    public String addVehicleByAdmin(Model model, @ModelAttribute("adminAdd_form") @Valid AdminAddForm adminAdd_form, BindingResult result,
                                    final RedirectAttributes redirectAttributes){
        int vehicleStatus;
        if(result.hasErrors()){
            return "AddVehicleAdmin";
        }
        try{
            vehicleStatus =  adminDAO.createVehicle(adminAdd_form);
            if(vehicleStatus == 1){
                model.addAttribute("errorMessage","Your vehicle is added!!!");
                List<VehicleForm> vehicleList = new ArrayList<VehicleForm>();
                vehicleList = adminDAO.getVehicleList();
                model.addAttribute("vehicleList",vehicleList);
                return "VehicleList";

            }
            else
            {
                model.addAttribute("errorMessage","Sorry, something went wrong, please try again");
                return "AddVehicleAdmin";
            }
        }
        catch(Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error:" + e.getMessage());
            return "AddVehicleAdmin";
        }

    }

    @RequestMapping(value="/updateVehicle", method = RequestMethod.POST)
    public String getUpdateVehiclePage(Model model, HttpServletRequest request){
        String userName = usersController.loginArray[0];
        model.addAttribute("userName",userName);
        List<VehicleForm> vehicleForm = new ArrayList<>();
        String vehicleNumber = request.getParameter("vehicleNo");
        vehicleForm = adminDAO.getDetailsOfVehicle(vehicleNumber);
        model.addAttribute("vehicleForm", vehicleForm);
        return "AdminUpdateVehicle";
    }

    @RequestMapping(value="/adminVehicleUpdate", method = RequestMethod.POST)
    public String updateVehicle(Model model, @ModelAttribute("vehicle_form") @Valid VehicleForm vehicle_form, BindingResult result,
                                final RedirectAttributes redirectAttributes, HttpServletRequest request){
        int vehicleStatus;
        vehicle_form.setVehicleNo(request.getParameter("vehicleNo"));
        vehicle_form.setVehicleType(request.getParameter("vehicleType"));
        vehicle_form.setEmail(request.getParameter("email"));
        vehicle_form.setMaxOcc(request.getParameter("maxOcc"));
        if(result.hasErrors()){
            return "AdminUpdateVehicle";
        }
        try{
            vehicleStatus = adminDAO.updateVehicle(vehicle_form);
            if(vehicleStatus == 1){
                model.addAttribute("errorMessage","Your vehicle is added!!!");
                List<VehicleForm> vehicleList = new ArrayList<VehicleForm>();
                vehicleList = adminDAO.getVehicleList();
                model.addAttribute("vehicleList",vehicleList);
                return "VehicleList";
            }
            else{
                model.addAttribute("errorMessage", "Sorry something went wrong, please try again");
                return "AdminUpdateVehicle";
            }

        }
        catch(Exception e){
            e.printStackTrace();
            return "AdminUpdateVehicle";
        }

    }

    @RequestMapping(value="/deleteVehicle", method = RequestMethod.GET)
    public String getDeleteVehiclePage(Model model, HttpServletRequest request){
        return "AdminDeleteVehicle";
    }

    @RequestMapping(value="/addStops", method = RequestMethod.GET)
    public String getAddStopsPage(Model model){
        StopsForm stopsForm = new StopsForm();
        List<String> vehicleNumber = new ArrayList<>();
        vehicleNumber = adminDAO.getVehicleNumber();
        model.addAttribute("vehicleNumber", vehicleNumber);
        model.addAttribute("stopsForm", stopsForm);
        return "AddStops";
    }

    @RequestMapping(value="/addStops", method = RequestMethod.POST)
    public String addStops(Model model, @ModelAttribute("stopsForm") @Valid StopsForm stopsForm, BindingResult result,
                           final RedirectAttributes redirectAttributes){
        int status = 0;
        if(result.hasErrors()){
            return "AddStops";
        }
        try {
            status = adminDAO.createStops(stopsForm);
            if (status == 1){
                List<StopsForm> stops_form = new ArrayList<>();
                stops_form = adminDAO.getStopList();
                model.addAttribute("stopsForm", stops_form);
                return "ManageStops";
            }

            else
                return "AddStops";
        }
        catch(Exception e){
            e.printStackTrace();
            return "AddStops";
        }
    }


    @RequestMapping(value="/manageStops", method = RequestMethod.GET)
    public String getStopsPage(Model model){
        List<StopsForm> stopsForm = new ArrayList<>();
        stopsForm = adminDAO.getStopList();
        model.addAttribute("stopsForm", stopsForm);
        return "ManageStops";
    }

    @RequestMapping(value="/updateStops", method = RequestMethod.POST)
    public String getUpdateRoutePage(Model model,HttpServletRequest request){
        List<StopsForm> stopsForm = new ArrayList<>();
        List<String> vehicleNumber = new ArrayList<>();
        String vehicleNo = request.getParameter("vehicleNo");
        routeVehicleNo = vehicleNo;
       stopsForm =  adminDAO.getRouteDetails(vehicleNo);
       vehicleNumber = adminDAO.getVehicleNumber();
       model.addAttribute("stopsForm", stopsForm);
       model.addAttribute("vehicleNumber",vehicleNumber);
        return "UpdateRoute";
    }

    @RequestMapping(value="/updateRouteDetails", method = RequestMethod.POST)
    public String updateRoute(Model model,@ModelAttribute("stops_form") @Valid StopsForm stops_form, BindingResult result,
                              final RedirectAttributes redirectAttributes, HttpServletRequest request){
        int routeStatus;
        //model.addAttribute("stops_form", stops_form);
        stops_form.setFromPlace(request.getParameter("source"));
        stops_form.setToPlace(request.getParameter("destination"));
        stops_form.setCost(request.getParameter("cost"));
        stops_form.setVehicleNo(request.getParameter("vehicleNo"));
        try{
            routeStatus = adminDAO.updateRoute(stops_form, routeVehicleNo);
            if(routeStatus == 1){
                List<StopsForm> stopsForm = new ArrayList<>();
                stopsForm = adminDAO.getStopList();
                model.addAttribute("stopsForm", stopsForm);
                return "ManageStops";
            }
            else
                return "UpdateRoute";
        }
        catch(Exception e){
            e.printStackTrace();
            return "updateRoute";
        }
    }
}
