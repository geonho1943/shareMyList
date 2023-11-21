package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.model.Potalgate;
import com.geonho1943.sharemylist.repository.PotalgateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PotalgateService {
    @Autowired
    private PotalgateRepository potalgateRepository;

    public List<Potalgate> getAllPotal(){
        return potalgateRepository.findAll();
    }

    public String youtubeLinkParser(String youtubeLink) {
        String videoId = null;

        //TODO 정규식으로 전환 하는것이 더 좋을듯하다.
        if (youtubeLink.startsWith("https://youtu.be/")) {
            // YouTube 단축 URL 형태 https://youtu.be/
            videoId = youtubeLink.substring("https://youtu.be/".length(), "https://youtu.be/".length() + 11);
        } else if (youtubeLink.startsWith("https://www.youtube.com/watch?v=")) {
            // YouTube 정식 URL 형태 https://www.youtube.com/watch?v=
            videoId = youtubeLink.substring("https://www.youtube.com/watch?v=".length(), "https://www.youtube.com/watch?v=".length() + 11);
        }
        return videoId;
    }

    public Potalgate getMetaDataByYoutAPI(String videoId){
        Potalgate videoMetaData = null;
        //TODO api요청부분
        return videoMetaData;
    }

    public void saveVideoMetaData(Potalgate videoMetaData) {
        //TODO DB 저장 JPA로 구현
    }
}
