package com.geonho1943.sharemylist.controller;

import com.geonho1943.sharemylist.model.User;
import com.geonho1943.sharemylist.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/")
    public String main(){
        return "home";
    }

    @RequestMapping("/test")
    @ResponseBody
    public List<User> test() {
        return userService.getAllUsers();
    }

}
