package com.geonho1943.sharemylist.controller;

import com.geonho1943.sharemylist.dto.CardDto;
import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.service.CardService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private CardService cardService;

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/")
    public String main(HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");

        if (loggedInUserInfo != null) {
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
        }
        try {
            List<CardDto> pt = cardService.getAllCard();
            Collections.reverse(pt);
            model.addAttribute("cardList", pt);
        }catch (Exception e){
            System.out.println(e);
        }
        return "home";
    }

}
