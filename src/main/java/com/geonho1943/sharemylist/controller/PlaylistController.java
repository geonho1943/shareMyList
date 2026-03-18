package com.geonho1943.sharemylist.controller;

import com.geonho1943.sharemylist.dto.CardDto;
import com.geonho1943.sharemylist.dto.PlaylistDto;
import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.service.CardService;
import com.geonho1943.sharemylist.service.PlaylistService;
import com.geonho1943.sharemylist.service.RecordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PlaylistController {
    private final CardService cardService;
    private final PlaylistService playlistService;
    private final RecordService recordService;
    private final SessionUserHelper sessionUserHelper;
    private final RequestSecurityHelper requestSecurityHelper;

    public PlaylistController(CardService cardService, PlaylistService playlistService, RecordService recordService, SessionUserHelper sessionUserHelper, RequestSecurityHelper requestSecurityHelper) {
        this.cardService = cardService;
        this.playlistService = playlistService;
        this.recordService = recordService;
        this.sessionUserHelper = sessionUserHelper;
        this.requestSecurityHelper = requestSecurityHelper;
    }

    private void addPlaylistOptions(int userIdx, Model model) {
        List<PlaylistDto> userPlayList = playlistService.getPlaylistByUser(userIdx);
        if (userPlayList.isEmpty()) {
            model.addAttribute("error", "emptyPlaylist");
        } else {
            model.addAttribute("playlistByUser", userPlayList);
        }
    }

    @GetMapping("/playlist")
    public String playlist(HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = sessionUserHelper.addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        addPlaylistOptions(loggedInUserInfo.getUserIdx(), model);
        return "playlist/playlist";
    }

    @GetMapping("/createplaylist")
    public String createPlaylistPage(HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = sessionUserHelper.addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        requestSecurityHelper.addCreatePlaylistCsrfToken(httpSession, model);
        return "playlist/createplaylist";
    }

    @PostMapping("/createplaylist")
    public String createPlaylist(@RequestParam("playlistName") String playlistName,
                                 @RequestParam(value = "csrfToken", required = false) String csrfToken,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 HttpSession httpSession,
                                 Model model){
        try {
            requestSecurityHelper.validateCreatePlaylistRequest(request, httpSession, csrfToken);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            model.addAttribute("error", e.getMessage());
            UserDto loggedInUserInfo = sessionUserHelper.getLoggedInUser(httpSession);
            if (loggedInUserInfo == null) {
                return "error/invalidrequest";
            }
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
            requestSecurityHelper.addCreatePlaylistCsrfToken(httpSession, model);
            return "playlist/createplaylist";
        }

        UserDto loggedInUserInfo = sessionUserHelper.getLoggedInUser(httpSession);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        playlistService.createPlaylist(loggedInUserInfo.getUserIdx(), playlistName);
        recordService.recordCreatePlaylist(loggedInUserInfo.getUserIdx());
        return "redirect:/playlist";
    }

    @GetMapping("/playlistInfo/{playlistIdx}")
    public String playlistInfo (@PathVariable int playlistIdx, HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = sessionUserHelper.addUserInfoToModel(httpSession, model);
        try {
            List<CardDto> cardInfoList = cardService.findCardsByPlaylist(playlistIdx);
            if (loggedInUserInfo != null) {
                recordService.recordCheckPlaylist(loggedInUserInfo.getUserIdx());
            }
            if (cardInfoList.isEmpty()){
                model.addAttribute("emptyData","emptyCardInfo");
            }else {
                model.addAttribute("cardInfoList",cardInfoList);
            }
            return "playlist/playlistinfo";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "playlist/playlistinfo";
        }
    }

    @RequestMapping("/deletePlaylist/{playlistIdx}")
    public String deletePlaylist(@PathVariable int playlistIdx, HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = sessionUserHelper.getLoggedInUser(httpSession);
        if (loggedInUserInfo == null){
            model.addAttribute("error", "emptyUserInfo");
            return "user/userlogin";
        }
        try {
            playlistService.deactivatePlaylist(playlistIdx, loggedInUserInfo.getUserIdx());
            recordService.recordDeletePlaylist(loggedInUserInfo.getUserIdx());
            return "redirect:/playlist";
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        addPlaylistOptions(loggedInUserInfo.getUserIdx(), model);
        return "playlist/playlist";
    }

}
