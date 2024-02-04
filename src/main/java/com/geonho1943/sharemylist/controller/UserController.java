package com.geonho1943.sharemylist.controller;

import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userlogin")
    public String userLogin() {
        return "user/userlogin";
    }

    @PostMapping("/userlogin")
    public String userLogin(HttpSession httpSession, HttpServletRequest request, UserDto loginInfo, Model model){
        try {
            UserDto checkedUserInfo = userService.userLogin(loginInfo);
            //로그인 정보가 데이터베이스의 유저정보와 일치한다면 유저정보 반환
            if (httpSession.getAttribute("checkedUserInfo") != null) {
                //반환된 유저 정보가 기존 세션에 있는지 체크
                httpSession.invalidate();
                //중복 로그인 방지를 위한 기존 세션 삭제
                httpSession= request.getSession();
                //새로운 세션 생성
            }
            httpSession.setAttribute("checkedUserInfo", checkedUserInfo);
            //세션을 추가, 로그인 성공
            return "redirect:/";
        }catch (Exception e){
            //userService.login 에서 문제 발생시 예외처리
            model.addAttribute("error", "failedLoginFromUserInfo");
            return "user/userlogin";
        }
    }

    @GetMapping("/logout")
    public String userLogout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:/";
    }

    @GetMapping("/resign")
    public String userResign(HttpSession httpSession){
        return "user/userresign";
    }

    @PostMapping("/resign")
    public String userResign(HttpSession httpSession, Model model, UserDto resignInfo){
        try {
            userService.resgin(resignInfo);
            httpSession.invalidate();
            model.addAttribute("success", "userResignSuccess");
            return "user/userlogin";
        }catch (Exception e){
            //userService.resgin 에서 문제 발생시 예외처리
            model.addAttribute("error", "failedResginFromUserInfo");
            return "user/userlogin";
        }

    }

}