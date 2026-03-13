package com.geonho1943.sharemylist.controller;

import com.geonho1943.sharemylist.dto.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class SessionUserHelper {

    public UserDto addUserInfoToModel(HttpSession session, Model model) {
        UserDto loggedInUserInfo = getLoggedInUser(session);
        if (loggedInUserInfo != null) {
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
        } else {
            model.addAttribute("error", "emptyUserInfo");
        }
        return loggedInUserInfo;
    }

    public UserDto getLoggedInUser(HttpSession session) {
        return (UserDto) session.getAttribute("checkedUserInfo");
    }
}
