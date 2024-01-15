package com.geonho1943.sharemylist.controller;

import com.geonho1943.sharemylist.dto.CardDto;
import com.geonho1943.sharemylist.dto.PlaylistDto;
import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.service.CardService;

import com.geonho1943.sharemylist.service.PlaylistService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CardController {

    private final Logger logger = LoggerFactory.getLogger(CardController.class);
    @Autowired
    private CardService cardService;

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/linkupload")
    public String linkUpload(HttpSession httpSession, Model model) {
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        if (loggedInUserInfo != null) {
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
            return "playlistImfomation/linkupload";
        }else {
            model.addAttribute("error", "emptyUserInfo");
            return "userInfomation/userlogin";
        }
    }

    @GetMapping("/setplaylist")
    public String playlistUpload(HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        if (loggedInUserInfo != null) {
            List<PlaylistDto> userPlayList = playlistService.getPlaylistByOneUser(loggedInUserInfo.getUserIdx());
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
            model.addAttribute("playlistByUser", userPlayList);
            //유저의 playlist 목록 조회
            return "playlistImfomation/setplaylist";
        }else {
            model.addAttribute("error", "emptyUserInfo");
            return "userInfomation/userlogin";
        }
    }

    @PostMapping("/submitYoutubeLink")
    public String parseAndSaveMetaData(@RequestParam("youtubeLink") String youtubeLink) {
        String videoId = cardService.youtubeLinkParser(youtubeLink);
        // URL 파싱
        CardDto videoMetaData = cardService.getMetaDataByYoutAPI(videoId);
        // youtube data api 요청 > json 값을 파싱해서 card 객체 생성
        cardService.saveVideoMetaData(videoMetaData);
        //  DB에 저장 (jpa)
        return "redirect:/";
    }
    @GetMapping("createplaylist")
    public String createPlayList(HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        if (loggedInUserInfo != null) {
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
            return "playlistImfomation/createplaylist";
        }else {
            model.addAttribute("error", "emptyUserInfo");
            return "userInfomation/userlogin";
        }
    }


}
