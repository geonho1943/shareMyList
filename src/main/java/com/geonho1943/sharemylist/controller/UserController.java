package com.geonho1943.sharemylist.controller;

import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @GetMapping("/userlogin")
    public String userLogin() {
        // TODO 이미 로그인 정보가 있는지 확인하는 메서드 구현
        return "userInfomation/userLogin";
    }

    @PostMapping("/userlogin")
    public String userlogin(HttpSession httpSession, UserDto loginInfo, Model model){
        try {
            UserDto loggedInUser = UserService.login(loginInfo);
            //TODO 세션이 이미 있다면 세션을 갱신
            httpSession.setAttribute("loggedInUser", loggedInUser);
            return "redirect:/";
        }catch (Exception e){
            //UserService.login 에서 문제 발생시 예외처리
            model.addAttribute("loginError", "try again");
            return "login";
        }

    }

}