package com.geonho1943.sharemylist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/userlogin")
    public String userlogin() {
        return "userInfomation/userLogin";
    }
}
