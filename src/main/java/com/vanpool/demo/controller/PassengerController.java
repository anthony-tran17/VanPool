package com.vanpool.demo.controller;

import com.vanpool.demo.formbean.RidesForm;
import com.vanpool.demo.formbean.StopsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.vanpool.demo.DAO.PassengersDAO;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PassengerController {

    @Autowired
    UsersController usersController;
    @Autowired
    PassengersDAO passengersDAO;

    String passengerCount;
   StopsForm globalStopsForm = new StopsForm();
    String userName;

    @RequestMapping(value="/passengerHome", method = RequestMethod.GET)
    public String returnPassengerPage(Model model){
        String userName = usersController.loginArray[0];
        model.addAttribute("userName", userName);
        return "PassengerHomePage";
    }


   @RequestMapping(value="/viewHistory", method = RequestMethod.GET)
    public String viewHistory(Model model){

        String userName = usersController.loginArray[0];
        model.addAttribute("userName",userName);
        List<RidesForm> rideList = new ArrayList<RidesForm>();
        rideList = passengersDAO.getRideList(usersController.email);
        model.addAttribute("ridelist",rideList);
        return "ViewRides";
    }

    @RequestMapping(value = "/bookRide", method = RequestMethod.GET)
    public String bookRide(Model model){
        userName = usersController.loginArray[0];
        model.addAttribute("userName",userName);
        List<RidesForm> rideList = new ArrayList<RidesForm>();
        rideList = passengersDAO.getRideList(userName);
        model.addAttribute("ridelist",rideList);
        
        List<String> fromStop = new ArrayList<String>();
        fromStop= passengersDAO.getFromStopNames();
        model.addAttribute("fromStop",fromStop);
        List<String> toStop = new ArrayList<>();
        toStop = passengersDAO.getToStopNames();
        model.addAttribute("toStop",toStop);
        List<Integer> fstopIds = passengersDAO.getStopIds();
        return "BookRide";
    }

    @RequestMapping(value="/bookRide", method = RequestMethod.POST)
    public String findRide(Model model, @ModelAttribute("stopsForm") @Valid StopsForm stopsForm, BindingResult result,
                           final RedirectAttributes redirectAttributes, HttpServletRequest request){
        userName = usersController.loginArray[0];
        passengerCount = "NULL";
        List<StopsForm> stops_form = new ArrayList<>();
        String fromStop = request.getParameter("source");
        String toStop = request.getParameter("destination");
        passengerCount = request.getParameter("passengerCount");
        stopsForm.setCountOfPassengers(passengerCount);
        globalStopsForm.setFromPlace(fromStop);
        globalStopsForm.setToPlace(toStop);
        globalStopsForm.setCountOfPassengers(passengerCount);
        try {
            stops_form = passengersDAO.checkForRide(fromStop, toStop, passengerCount);
            model.addAttribute("rideList", stops_form);
            return "AvailableRide";
        }
        catch(Exception e){
            e.printStackTrace();
            return "BookRide";
        }
    }

    @RequestMapping(value="/confirmRide", method=RequestMethod.POST)
    public String confirmRide(Model model,HttpServletRequest request){
        int rideStatus;
        userName = usersController.email;
        String vehicleNo = request.getParameter("vehicleNo");
        globalStopsForm.setVehicleNo(vehicleNo);
        try{
            rideStatus = passengersDAO.insertRides(vehicleNo,passengerCount,globalStopsForm,userName);
            if(rideStatus == 1) {
                model.addAttribute("rideDetails", globalStopsForm);
                return "ConfirmPage";
            }
            else
            {
                model.addAttribute("rideDetails","Sorry couldn't book your ride, please try again later");
                return "BookRide";
            }

        }
        catch(Exception e){
            e.printStackTrace();
            return "BookRide";
        }

    }
}
