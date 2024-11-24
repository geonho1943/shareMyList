package com.geonho1943.sharemylist.controller;

import com.geonho1943.sharemylist.dto.CardDto;
import com.geonho1943.sharemylist.dto.PlaylistDto;
import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.service.CardService;
import com.geonho1943.sharemylist.service.PlaylistService;
import com.geonho1943.sharemylist.service.RecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PlaylistController {

    @Autowired
    private CardService cardService;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private RecordService recordService;

    private UserDto addUserInfoToModel(HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        if (loggedInUserInfo != null) {
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
        }else {
            model.addAttribute("error", "emptyUserInfo");
        }
        return loggedInUserInfo;
    }

    @GetMapping("/playlist")
    public String playlistUpload(HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        List<PlaylistDto> userPlayList = playlistService.getPlaylistByUser(loggedInUserInfo.getUserIdx());
        if (userPlayList.isEmpty()) model.addAttribute("error", "emptyPlaylist");
        else model.addAttribute("playlistByUser", userPlayList);
        return "playlist/playlist";
    }

    @GetMapping("/createplaylist")
    public String createPlayList(HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        return "playlist/createplaylist";
    }

    @PostMapping("/createplaylist")
    public String createPlaylist(HttpSession httpSession, String playlistName){
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        playlistService.createPlaylist(loggedInUserInfo.getUserIdx(), playlistName);
        recordService.recordCreatePlaylist(loggedInUserInfo.getUserIdx());
        return "redirect:/playlist";
    }

    @GetMapping("/playlistInfo/{playlistIdx}")
    public String playlistInfo (@PathVariable int playlistIdx, HttpSession httpSession, Model model){
        //playlist의 card 조회
        UserDto loggedInUserInfo = addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        List<CardDto> cardInfoList = cardService.findCardsByPlaylist(playlistIdx);
        recordService.recordCheckPlaylist(loggedInUserInfo.getUserIdx());
        if (cardInfoList.isEmpty()){
            model.addAttribute("emptyData","emptyCardInfo");
        }else {
            model.addAttribute("cardInfoList",cardInfoList);
        }
        return "playlist/playlistinfo";
    }

    @RequestMapping("/deletePlaylist/{playlistIdx}")
    public String deletePlaylist(@PathVariable int playlistIdx, HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        try {
            // 유저 검즘
            if (!playlistService.isValidatePlaylist(playlistIdx, loggedInUserInfo.getUserIdx())){
                model.addAttribute("error","userCheck");
                return "/playlist";
            }
            // plstlist 비활성화
            playlistService.deactivatePlaylist(playlistIdx, loggedInUserInfo.getUserIdx());
            recordService.recordDeletePlaylist(loggedInUserInfo.getUserIdx());
            return "redirect:/playlist";
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return "/playlist";
    }

}
