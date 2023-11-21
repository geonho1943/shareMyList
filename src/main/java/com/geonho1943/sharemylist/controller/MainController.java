package com.geonho1943.sharemylist.controller;

import com.geonho1943.sharemylist.model.Potalgate;
import com.geonho1943.sharemylist.model.User;
import com.geonho1943.sharemylist.service.PotalgateService;
import com.geonho1943.sharemylist.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserService userService;
    @Autowired
    private PotalgateService potalgateService;

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/")
    public String main(Model model){
        try {
            List<Potalgate> pt = potalgateService.getAllPotal();
            model.addAttribute("potalList", pt);
        }catch (Exception e){
            System.out.println(e);
        }
        return "home";
    }

    @RequestMapping("/test")
    @ResponseBody
    public List<User> test() {
        System.out.println("test 성공");
        return userService.getAllUsers();
    }

}
