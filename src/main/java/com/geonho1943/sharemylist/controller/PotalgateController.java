package com.geonho1943.sharemylist.controller;

import com.geonho1943.sharemylist.dto.CardDto;
import com.geonho1943.sharemylist.model.Card;
import com.geonho1943.sharemylist.service.CardService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class PotalgateController {

    private final Logger logger = LoggerFactory.getLogger(PotalgateController.class);
    @Autowired
    private CardService cardService;

    @GetMapping("/linkupload")
    public String linkUpload() {
        return "playlistImfomation/linkUpload";
    }

    @PostMapping("/submitYoutubeLink")
    public String parseAndSaveMetaData(@RequestParam("youtubeLink") String youtubeLink) {
        String videoId = cardService.youtubeLinkParser(youtubeLink);
        // URL 파싱
        CardDto videoMetaData = cardService.getMetaDataByYoutAPI(videoId);
        // youtube data api 요청 > json 값을 파싱해서 Potalgate 객체 생성
        cardService.saveVideoMetaData(videoMetaData);
        //  DB에 저장 (jpa)
        return "redirect:/";
    }

}
