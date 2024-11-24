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
public class CardController {

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
    // parseAndSaveMetaData fail 시 modal 유지 메서드
    private void addModalForFailSubmit(UserDto loggedInUserInfo, Model model) {
        List<PlaylistDto> userPlayList = playlistService.getPlaylistByUser(loggedInUserInfo.getUserIdx());
        if (userPlayList.isEmpty()) {
            model.addAttribute("error", "emptyPlaylist");
        } else {
            model.addAttribute("playlistByUser", userPlayList);
        }
    }

    @RequestMapping("/")
    public String home(HttpSession httpSession, Model model){
        addUserInfoToModel(httpSession, model);
        List<CardDto> cards = cardService.getAllCard();
        if (cards.isEmpty()){
            model.addAttribute("emptyData","emptyCard");
            return "home";
        }
        model.addAttribute("cardList", cards);
        return "home";
    }

    @RequestMapping("/search")
    public String search(@RequestParam("keyword") String keyword, HttpSession httpSession, Model model) {
        addUserInfoToModel(httpSession, model);
        List<CardDto> cardsBySearch = cardService.findAllCardByTitle(keyword);
        if (cardsBySearch.isEmpty()){
            model.addAttribute("emptyData","emptyCard");
            return "home";
        }
        model.addAttribute("cardsBySearch", cardsBySearch);
        return "home";
    }

    @GetMapping("/linkupload")
    public String linkUpload(HttpSession httpSession, Model model) {
        UserDto loggedInUserInfo = addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        addModalForFailSubmit(loggedInUserInfo, model);
        return "playlist/linkupload";
    }

    @PostMapping("/submitYoutubeLink")
    public String parseAndSaveMetaData(@RequestParam("youtubeLink") String youtubeLink, @RequestParam("playlistIdx") int playlistIdx, HttpSession httpSession, Model model) {
        UserDto loggedInUserInfo = addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null) return "user/userlogin";
        try {
            String videoId = cardService.youtubeLinkParser(youtubeLink);
            CardDto videoMetaData = cardService.getMetaDataByYoutApi(videoId);
            cardService.saveCardToPlaylist(videoMetaData, playlistIdx);
            recordService.recordCreateCard(loggedInUserInfo.getUserIdx());
            return "redirect:/playlistInfo/" + playlistIdx;
        }catch (IllegalArgumentException e){
            model.addAttribute("error", "noVideoMetaDate");
        } catch (Exception e) {
            model.addAttribute("error", "invalidYouTubeLink");
        }
        addModalForFailSubmit(loggedInUserInfo, model);
        return "playlist/linkupload";
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

    @RequestMapping("/deleteCard/{cardIdx}")
    public String deleteCard(@PathVariable int cardIdx, HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null) return "user/userlogin";
        try {
            CardDto cardInfo = cardService.getCardInfo(cardIdx);
            if (playlistService.isValidatePlaylist(cardInfo.getPlaylist().getPlaylistIdx(), loggedInUserInfo.getUserIdx())){
                cardService.deactivateCard(cardIdx);
                recordService.recordDeleteCard(loggedInUserInfo.getUserIdx());
                return "redirect:/playlistInfo/" + cardInfo.getPlaylist().getPlaylistIdx();
            }
        }catch (Exception e){
            model.addAttribute("error", "failedDeleteByCardInfo");
        }
        return "playlistinfo";
    }

}
