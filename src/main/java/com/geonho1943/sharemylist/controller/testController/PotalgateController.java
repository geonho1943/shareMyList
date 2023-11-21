package com.geonho1943.sharemylist.controller.testController;

import com.geonho1943.sharemylist.model.Potalgate;
import com.geonho1943.sharemylist.service.PotalgateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PotalgateController {

    private final Logger logger = LoggerFactory.getLogger(PotalgateController.class);
    @Autowired
    private PotalgateService potalgateService;

    @GetMapping("/linkupload")
    public String linkupload() {
        return "playlistImfomation/linkUpload";
    }

    @GetMapping("/stopsample")
    public String sampleGetMapping(Model model) {

        try {
            List<Potalgate> pt = potalgateService.getAllPotal();
            model.addAttribute("potalList", pt);
        }catch (Exception e){
            System.out.println(e);
        }
        return "sample/stopsample";
    }

    @PostMapping("/submitYoutubeLink")
    public String parseAndSaveMetaData(@RequestParam("youtubeLink") String youtubeLink) {
        String videoId = potalgateService.youtubeLinkParser(youtubeLink);
        System.out.println(videoId);
        // URL 파싱
        Potalgate videoMetaData = potalgateService.getMetaDataByYoutAPI(videoId);
        // youtube data api 요청
        potalgateService.saveVideoMetaData(videoMetaData);
        //  url data 저장
        return "redirect:/";
    }


}

