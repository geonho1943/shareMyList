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

import java.util.Collections;
import java.util.List;

@Controller
public class PlaylistController {

    @Autowired
    private CardService cardService;
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private RecordService recordService;

    private UserDto addUserInfoToModel(HttpSession httpSession, Model model) {
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        if (loggedInUserInfo != null) {
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
        }else {
            model.addAttribute("error", "emptyUserInfo");
        }
        return loggedInUserInfo;
    }

    @RequestMapping("/")
    public String home(HttpSession httpSession, Model model){
        addUserInfoToModel(httpSession, model);
        List<CardDto> pt = cardService.getAllCard();
        Collections.reverse(pt);
        model.addAttribute("cardList", pt);
        return "home";
    }
    @RequestMapping("/search")
    public String search(@RequestParam("keyword") String keyword, HttpSession httpSession, Model model) throws Exception {
        addUserInfoToModel(httpSession, model);
        List<CardDto> cardList = cardService.findAllCardByTitle(keyword);
        Collections.reverse(cardList);
        model.addAttribute("cardList", cardList);
        return "home";
    }
    @GetMapping("/linkupload")
    public String linkUpload(HttpSession httpSession, Model model) {
        UserDto loggedInUserInfo = addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        List<PlaylistDto> userPlayList = playlistService.getPlaylistByOneUser(loggedInUserInfo.getUserIdx());
        Collections.reverse(userPlayList);
        if (userPlayList.isEmpty()) model.addAttribute("error", "emptyPlaylist");
        else model.addAttribute("playlistByUser", userPlayList);
        return "playlist/linkupload";
    }

    @GetMapping("/playlist")
    public String playlistUpload(HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        List<PlaylistDto> userPlayList = playlistService.getPlaylistByOneUser(loggedInUserInfo.getUserIdx());
        Collections.reverse(userPlayList);
        if (userPlayList.isEmpty()) model.addAttribute("error", "emptyPlaylist");
        else model.addAttribute("playlistByUser", userPlayList);
        return "playlist/playlist";
    }

    @PostMapping("/submitYoutubeLink")
    public String parseAndSaveMetaData(@RequestParam("youtubeLink") String youtubeLink, @RequestParam("playlistIdx") int playlistIdx, HttpSession httpSession) {
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        String videoId = cardService.youtubeLinkParser(youtubeLink);
        // URL 파싱
        CardDto videoMetaData = cardService.getMetaDataByYoutAPI(videoId);
        // youtube data api 요청 > json 값을 파싱해서 card 객체 생성
        videoMetaData.setCardPlaylistIdx(playlistIdx);
        //유저가 선택한 playlistIdx 추가
        cardService.saveVideoMetaData(videoMetaData);
        //  DB에 저장
        recordService.recordCreateCard(loggedInUserInfo.getUserIdx());
        // card 생성 로그 저장
        return "redirect:/";
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
    public String createPlaylist(HttpSession httpSession, Model model, String playlistName){
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        //유저정보 확인
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        playlistService.createPlaylist(loggedInUserInfo.getUserIdx(),playlistName);
        recordService.recordCreatePlaylist(loggedInUserInfo.getUserIdx());
        return "redirect:/playlist";
    }

    @GetMapping("/cardInfo/{cardIdx}")
    public String cardInfo (@PathVariable int cardIdx, HttpSession httpSession, Model model){
        //card 조회
        UserDto loggedInUserInfo = addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        CardDto cardInfo = cardService.getCardInfo(cardIdx);
        recordService.recordCheckCard(loggedInUserInfo.getUserIdx());
        model.addAttribute("cardInfo", cardInfo);
        return "card/cardinfo";

    }

    @GetMapping("/playlistInfo/{playlistIdx}")
    public String playlistInfo (@PathVariable int playlistIdx, HttpSession httpSession, Model model){
        //playlist의 card 조회
        UserDto loggedInUserInfo = addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        List<CardDto> cardInfoList = cardService.getCardListByPlaylist(playlistIdx);
        recordService.recordCheckPlaylist(loggedInUserInfo.getUserIdx());
        Collections.reverse(cardInfoList);
        if (cardInfoList.isEmpty()){
            model.addAttribute("emptyData","emptyCardInfo");
        }else {
            model.addAttribute("cardInfoList",cardInfoList);
        }
        return "playlist/playlistinfo";
    }

    @RequestMapping("/deleteCard/{cardPlaylistIdx}/{cardIdx}")
    public String deleteCard(@PathVariable int cardPlaylistIdx, @PathVariable int cardIdx, HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        int verifeduserIdx = playlistService.verifyposs(cardPlaylistIdx);
        if (verifeduserIdx == loggedInUserInfo.getUserIdx()){
            cardService.deleteCard(cardIdx);
            recordService.recordDeleteCard(verifeduserIdx);
        }else {
            model.addAttribute("error", "failedDeleteByIncorInfo");
            return "playlist/playlistinfo";
        }
        return "redirect:/";
    }

    @RequestMapping("/deletePlaylist/{playlistIdx}")
    public String deletePlaylist(@PathVariable int playlistIdx, HttpSession httpSession, Model model) throws Exception {
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        try {
            playlistService.deletePlaylist(playlistIdx, loggedInUserInfo.getUserIdx());
            recordService.recordDeletePlaylist(loggedInUserInfo.getUserIdx());
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/playlist";
    }

}
