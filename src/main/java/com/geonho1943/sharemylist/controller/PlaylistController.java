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

    @GetMapping("/linkupload")
    public String linkUpload(HttpSession httpSession, Model model) {
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        if (loggedInUserInfo != null) {
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
            List<PlaylistDto> userPlayList = playlistService.getPlaylistByOneUser(loggedInUserInfo.getUserIdx());
            //유저의 playlist 목록 조회
            Collections.reverse(userPlayList);
            if (userPlayList.size() != 0){
                model.addAttribute("playlistByUser", userPlayList);
            }else {
                model.addAttribute("error", "emptyPlaylist");
            }
            return "playlist/linkupload";
        }else {
            model.addAttribute("error", "emptyUserInfo");
            return "user/userlogin";
        }
    }

    @GetMapping("/playlist")
    public String playlistUpload(HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        if (loggedInUserInfo != null) {
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
            List<PlaylistDto> userPlayList = playlistService.getPlaylistByOneUser(loggedInUserInfo.getUserIdx());
            //유저의 playlist 목록 조회
            Collections.reverse(userPlayList);
            if (userPlayList.size() != 0){
                model.addAttribute("playlistByUser", userPlayList);
            }else {
                model.addAttribute("error", "emptyPlaylist");
            }
            return "playlist/playlist";
        }else {
            model.addAttribute("error", "emptyUserInfo");
            return "user/userlogin";
        }
    }

    @PostMapping("/submitYoutubeLink")
    public String parseAndSaveMetaData(@RequestParam("youtubeLink") String youtubeLink, @RequestParam("playlistIdx") int playlistIdx) {
        String videoId = cardService.youtubeLinkParser(youtubeLink);
        // URL 파싱
        CardDto videoMetaData = cardService.getMetaDataByYoutAPI(videoId);
        // youtube data api 요청 > json 값을 파싱해서 card 객체 생성
        videoMetaData.setCardPlaylistIdx(playlistIdx);
        //유저가 선택한 playlistIdx 추가
        cardService.saveVideoMetaData(videoMetaData);
        //  DB에 저장 (jpa)
        return "redirect:/";
    }

    @GetMapping("/createplaylist")
    public String createPlayList(HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        if (loggedInUserInfo != null) {
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
            return "playlist/createplaylist";
        }else {
            model.addAttribute("error", "emptyUserInfo");
            return "user/userlogin";
        }
    }

    @PostMapping("/createplaylist")
    public String createPlaylist(HttpSession httpSession, Model model, String playlistName){
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        //유저정보 확인
        if (loggedInUserInfo != null) {
            try {
                playlistService.createPlaylist(loggedInUserInfo.getUserIdx(),playlistName);
                recordService.createPlaylistLog(loggedInUserInfo.getUserIdx());
            }catch (Exception e){
                throw e;
            }
            return "redirect:/playlist";
        }else {
            model.addAttribute("error", "emptyUserInfo");
            return "user/userlogin";
        }

    }

    @GetMapping("/cardInfo/{cardIdx}")
    public String cardInfo (@PathVariable int cardIdx, HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        if (loggedInUserInfo != null) {
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
            CardDto cardInfo = cardService.getCardInfo(cardIdx);
            model.addAttribute("cardInfo", cardInfo);
            return "card/cardinfo";
        }else {
            model.addAttribute("error", "emptyUserInfo");
            return "user/userlogin";
        }
    }

    @GetMapping("/playlistInfo/{playlistIdx}")
    public String playlistInfo (@PathVariable int playlistIdx, HttpSession httpSession, Model model){
        //playlist의 card 조회
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        if (loggedInUserInfo != null) {
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
        }else {
            model.addAttribute("error", "emptyUserInfo");
            return "user/userlogin";
        }
        List<CardDto> cardInfoList = cardService.getCardListByPlaylist(playlistIdx);
        recordService.checkPlaylistLog(loggedInUserInfo.getUserIdx());
        Collections.reverse(cardInfoList);
        if (!cardInfoList.isEmpty()){
            model.addAttribute("cardInfoList",cardInfoList);
        }else {
            model.addAttribute("emptyData","emptyCardInfo");
        }
        return "playlist/playlistinfo";

    }


    @RequestMapping("/deleteCard/{cardPlaylistIdx}/{cardIdx}")
    public String deleteCard(@PathVariable int cardPlaylistIdx, @PathVariable int cardIdx, HttpSession httpSession, Model model){
        //카드삭제 과정: 검증데이터 조회 > 검증 > 삭제
        //검증 데이터 조회
        //cardPlaylistIdx가 playlist테이블의 playlist_idx와 일치하는 필드의 playlist_useridx 조회
        //본인 검증
        //playlist_useridx가 세션의 loggedInUserInfo.userIdx 와 일치한다면 해당 유저가 생성했던 card 임을 검증하게 됨
        //카드 삭제
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        if (loggedInUserInfo != null) {
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
        }else {
            model.addAttribute("error", "emptyUserInfo");
            return "user/userlogin";
        }
        int verifeduserIdx = playlistService.verifyposs(cardPlaylistIdx);
        if (verifeduserIdx == loggedInUserInfo.getUserIdx()){
            cardService.deleteCard(cardIdx);
        }else {
            model.addAttribute("error", "failedDeleteByIncorInfo");
            return "playlist/playlistinfo";
        }
        return "redirect:/";
    }


    @RequestMapping("/deletePlaylist/{playlistIdx}")
    public String deletePlaylist(@PathVariable int playlistIdx, HttpSession httpSession) throws Exception {
        //플레이리스트삭제
        //TODO: PL레코드를 참조하는 card레코드 때문에 제약조건위반 페이지 대응 필요: (card가 먼저 삭제 되어야 합니다)
        UserDto loggedInUserInfo = (UserDto) httpSession.getAttribute("checkedUserInfo");
        playlistService.deletePlaylist(playlistIdx, loggedInUserInfo.getUserIdx());
        recordService.deletePlaylistLog(loggedInUserInfo.getUserIdx());
        return "redirect:/playlist";
    }

}
