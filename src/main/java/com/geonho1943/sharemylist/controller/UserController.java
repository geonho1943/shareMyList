package com.geonho1943.sharemylist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/userlogin")
    public String userLogin() {
        // TODO 이미 로그인 정보가 있는지 확인하는 메서드 구현
        return "userInfomation/userLogin";
    }
//
//    @PostMapping("/userlogin")
//    public String userlogin(HttpSession httpSession, User user){
//        //로그인 요청에 대한 검증
//            //세션에 해당 유저정보가 있는지 체크
//        UserDTO checkLogin = (UserDTO) httpSession.getAttribute("loginInfo");
//
//            //이미 있다면 이전 세션을 삭제 하고 모달창 띄우기(중복 로그인 : 다른기기에 접속된 계정이 로그아웃 됩니다.)
//
//            //새로운 세션으로 갱신
//        // 리다이렉트
//
//    }
}


//    private final UserService userService;

//    @Autowired
//    public LoginController(UserService userService) {
//        this.userService = userService;
//    }

//    @PostMapping("/userlogin")
//    public String userlogin(@ModelAttribute("loginForm") UserLoginForm loginForm, HttpSession session, Model model) {
//        // 로그인 기능 구현 메서드를 서비스에서 호출
//        UserDTO loggedInUser = userService.userLogin(loginForm.getUsername(), loginForm.getPassword());
//
//        if (loggedInUser != null) {
//            // 중복 로그인 방지를 위해 기존 세션 무효화
//            session.invalidate();
//
//            // 새로운 세션 생성 및 로그인 사용자 정보 저장
//            HttpSession newSession = request.getSession(true);
//            newSession.setAttribute("loggedInUser", loggedInUser);
//
//            return "redirect:/home";
//        } else {
//            model.addAttribute("loginError", "Invalid username or password");
//            return "login";
//        }
//    }
//
//    @PostMapping("/userlogout")
//    public String userLogout(HttpSession session) {
//        // 로그아웃 기능 구현 메서드를 서비스에서 호출
//        userService.userLogout((UserDTO) session.getAttribute("loggedInUser"));
//
//        // 세션 무효화
//        session.invalidate();
//        return "redirect:/login";
//    }
//
//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute("registrationForm") UserRegistrationForm registrationForm, Model model) {
//        // 회원가입 기능 구현 메서드를 서비스에서 호출
//        boolean registrationSuccess = userService.registerUser(registrationForm);
//
//        if (registrationSuccess) {
//            return "redirect:/login";
//        } else {
//            model.addAttribute("registrationError", "Registration failed. Please try again.");
//            return "register";
//        }
//    }
//}
