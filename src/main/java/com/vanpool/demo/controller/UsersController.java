package com.vanpool.demo.controller;

import com.vanpool.demo.DAO.UsersDAO;
import com.vanpool.demo.formbean.LoginForm;
import com.vanpool.demo.formbean.UsersForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;


@Controller
@SessionAttributes("userName")
public class UsersController {

    @Qualifier("Users")
    @Autowired
   UsersDAO userDAO;

    Errors errors;
    String[] loginArray = new String[2];
    String email = "";

    @RequestMapping("/")
    public String viewHome(Model model){
        if(loginArray == null)
            return "welcomePage";
        else {
            model.addAttribute("userName", loginArray[0]);
            return "welcomePage";
        }

    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String viewRegisterPage(Model model){
        UsersForm register_form = new UsersForm();
        model.addAttribute("users_form",register_form);
        return "registerPage";
    }

    @RequestMapping(value = "/register", method  = RequestMethod.POST)
    public String addUser(Model model,@ModelAttribute("users_form") @Valid UsersForm usersForm, BindingResult result, final RedirectAttributes redirectAttributes){
        int status;
        if(result.hasErrors()){
            return "registerPage";
        }
        try{
            status = userDAO.create(usersForm);
            if(status == 1){
                model.addAttribute("errorMessage","you have successfully registered!!!");
                return "redirect:/registerSuccess";
            }
            else
            {
                model.addAttribute("errorMessage","Sorry,email id already exists");
                return "registerPage";
            }
        }
        catch(Exception e){
            model.addAttribute("errorMessage", "Error:" + e.getMessage());
            return "registerPage";
        }


    }

    @RequestMapping("/registerSuccess")
    public String registerSuccess(Model model){
        LoginForm login_form = new LoginForm();
        model.addAttribute("login_form",login_form);
        return "Login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(Model model){
        LoginForm login_form = new LoginForm();
        model.addAttribute("login_form",login_form);
        return "Login";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String verifyLogin(Model model, @ModelAttribute("login_form") @Valid LoginForm loginForm, HttpServletRequest request){
        String status = "";
        HttpSession session = request.getSession(true);
       loginArray = userDAO.validateLogin(loginForm);
       if( (!(Arrays.asList(loginArray).contains("invalid"))) && (!(loginArray.length == 0))){
           email = loginForm.getEmail();
           status = loginArray[1];
           session.setAttribute("userName",loginArray[0]);
           model.addAttribute("userName", loginArray[0]);
       }
       switch (status){
           case "Driver":
               return "DriverHomePage";
           case "Passenger":
               return "PassengerHomePage";
           case "Administrator":
               return "AdminHomePage";
           case "invalid":
               model.addAttribute("errorMessage","Sorry, incorrect Email ID Or Password. Please try again");
               return "Login";
           default: break;

       }
        return "/loginSuccess";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(Model model){
        return "WelcomePage";
    }
}
