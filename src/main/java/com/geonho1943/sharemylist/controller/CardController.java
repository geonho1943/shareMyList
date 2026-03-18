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
public class CardController {
    private final CardService cardService;
    private final PlaylistService playlistService;
    private final RecordService recordService;
    private final SessionUserHelper sessionUserHelper;
    private final RequestSecurityHelper requestSecurityHelper;

    public CardController(CardService cardService, PlaylistService playlistService, RecordService recordService, SessionUserHelper sessionUserHelper, RequestSecurityHelper requestSecurityHelper) {
        this.cardService = cardService;
        this.playlistService = playlistService;
        this.recordService = recordService;
        this.sessionUserHelper = sessionUserHelper;
        this.requestSecurityHelper = requestSecurityHelper;
    }

    private void addPlaylistOptions(UserDto loggedInUserInfo, Model model) {
        List<PlaylistDto> userPlayList = playlistService.getPlaylistByUser(loggedInUserInfo.getUserIdx());
        if (userPlayList.isEmpty()) {
            model.addAttribute("error", "emptyPlaylist");
        } else {
            model.addAttribute("playlistByUser", userPlayList);
        }
    }

    @RequestMapping("/")
    public String home(HttpSession httpSession, Model model){
        sessionUserHelper.addUserInfoToModel(httpSession, model);
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
        sessionUserHelper.addUserInfoToModel(httpSession, model);
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
        UserDto loggedInUserInfo = sessionUserHelper.addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null){
            return "user/userlogin";
        }
        addPlaylistOptions(loggedInUserInfo, model);
        requestSecurityHelper.addSubmitYoutubeLinkCsrfToken(httpSession, model);
        return "playlist/linkupload";
    }

    @PostMapping("/submitYoutubeLink")
    public String submitYoutubeLink(@RequestParam("youtubeLink") String youtubeLink,
                                    @RequestParam("playlistIdx") int playlistIdx,
                                    @RequestParam(value = "csrfToken", required = false) String csrfToken,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    HttpSession httpSession,
                                    Model model) {
        try {
            requestSecurityHelper.validateSubmitYoutubeLinkRequest(request, httpSession, csrfToken);
        }catch (IllegalArgumentException e){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            model.addAttribute("error", e.getMessage());
            UserDto loggedInUserInfo = sessionUserHelper.getLoggedInUser(httpSession);
            if (loggedInUserInfo == null) {
                return "error/invalidrequest";
            }
            model.addAttribute("loggedInUserInfo", loggedInUserInfo);
            addPlaylistOptions(loggedInUserInfo, model);
            requestSecurityHelper.addSubmitYoutubeLinkCsrfToken(httpSession, model);
            return "playlist/linkupload";
        }

        UserDto loggedInUserInfo = sessionUserHelper.addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null) {
            return "user/userlogin";
        }

        try {
            String videoId = cardService.parseYoutubeVideoId(youtubeLink);
            CardDto videoMetaData = cardService.getMetadataByYoutubeApi(videoId);
            cardService.saveCardToPlaylist(videoMetaData, playlistIdx, loggedInUserInfo.getUserIdx());
            recordService.recordCreateCard(loggedInUserInfo.getUserIdx());
            return "redirect:/playlistInfo/" + playlistIdx;
        } catch (RuntimeException e) {
            model.addAttribute("error", "failedSaveCard");
        }
        addPlaylistOptions(loggedInUserInfo, model);
        requestSecurityHelper.addSubmitYoutubeLinkCsrfToken(httpSession, model);
        return "playlist/linkupload";
    }

    @GetMapping("/cardInfo/{cardIdx}")
    public String cardInfo (@PathVariable int cardIdx, HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = sessionUserHelper.addUserInfoToModel(httpSession, model);
        try {
            CardDto cardInfo = cardService.getCardInfo(cardIdx);
            if (loggedInUserInfo != null) {
                recordService.recordCheckCard(loggedInUserInfo.getUserIdx());
            }
            model.addAttribute("cardInfo", cardInfo);
            return "card/cardinfo";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "card/cardinfo";
        }
    }

    @RequestMapping("/deleteCard/{cardIdx}")
    public String deleteCard(@PathVariable int cardIdx, HttpSession httpSession, Model model){
        UserDto loggedInUserInfo = sessionUserHelper.addUserInfoToModel(httpSession, model);
        if (loggedInUserInfo == null) return "user/userlogin";
        try {
            int playlistIdx = cardService.deactivateCard(cardIdx, loggedInUserInfo.getUserIdx());
            recordService.recordDeleteCard(loggedInUserInfo.getUserIdx());
            return "redirect:/playlistInfo/" + playlistIdx;
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        List<PlaylistDto> userPlayList = playlistService.getPlaylistByUser(loggedInUserInfo.getUserIdx());
        if (userPlayList.isEmpty()) model.addAttribute("error", "emptyPlaylist");
        else model.addAttribute("playlistByUser", userPlayList);
        return "playlist/playlist";
    }

}
