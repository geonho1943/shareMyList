package com.geonho1943.sharemylist.controller;

import com.geonho1943.sharemylist.dto.PlaylistDto;
import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.service.CardService;
import com.geonho1943.sharemylist.service.PlaylistService;
import com.geonho1943.sharemylist.service.RecordService;
import com.geonho1943.sharemylist.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private CardService cardService;
    @Autowired
    private RecordService recordService;

    @GetMapping("/login")
    public String userLogin() {
        return "user/userlogin";
    }

    @PostMapping("/login")
    public String userLogin(HttpSession httpSession, HttpServletRequest request, UserDto loginInfo, Model model){
        try {
            UserDto checkedUserInfo = userService.verification(loginInfo);
            if (httpSession.getAttribute("checkedUserInfo") != null) {
                //유저 정보가 기존 세션에 있는지 체크
                httpSession.invalidate();
                httpSession= request.getSession();
            }
            httpSession.setAttribute("checkedUserInfo", checkedUserInfo);
            recordService.recordLogin(checkedUserInfo.getUserIdx());
            return "redirect:/";
        }catch (Exception e){
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
    public String userResign(){
        return "user/userresign";
    }

    @Transactional
    @PostMapping("/resign")
    public String userResign(HttpSession httpSession, Model model, UserDto resignInfo){
        try {
            UserDto checkedUserInfo = userService.verification(resignInfo);
            //검증
            userService.resign(checkedUserInfo, resignInfo.getUserPw());
            recordService.recordResign(checkedUserInfo.getUserIdx());
            // 유저 탈퇴 정보 저장
            // 계정 비활성화
            List<PlaylistDto> deleteListinfo = playlistService.getPlaylistByOneUser(checkedUserInfo.getUserIdx());
            if (!deleteListinfo.isEmpty()) {
                for (PlaylistDto playlistDto : deleteListinfo) {
                    playlistService.deactivatePlaylist(playlistDto.getPlaylistIdx(), checkedUserInfo.getUserIdx());
                }
            }
            httpSession.invalidate();
            model.addAttribute("success", "userResignSuccess");
            return "user/userlogin";
        }catch (Exception e){
            model.addAttribute("error", "failedResign");
            return "user/userlogin";
        }
    }

    @GetMapping("/join")
    public String userJoin(){
        return "user/userJoin";
    }

    @PostMapping("/join")
    public String userJoin(UserDto joinInfo, Model model){
        try {
            String errorReason = userService.checkAccount(joinInfo);
            if (errorReason!=null) {
                model.addAttribute("error", errorReason);
                return "user/userJoin";
            }
            if (userService.isDuplicateId(joinInfo.getUserId())) {
                model.addAttribute("error", "다른 id를 사용해주세요.");
                return "user/userJoin";
            }
            userService.saveAccount(joinInfo);
            recordService.recordJoin(joinInfo.getUserIdx());
            // 유저 가입 정보 저장
            model.addAttribute("userJoinSuccess", "회원가입에 성공했습니다.");
            return "user/userlogin";
        }catch (RuntimeException e){
            model.addAttribute("error", "회원가입중 문제가 발생했습니다 나중에 다시 시도해주세요");
            return "user/userJoin";
        }

    }

}