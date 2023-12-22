package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.dto.CardDto;
import com.geonho1943.sharemylist.model.Card;
import com.geonho1943.sharemylist.repository.CardRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    public List<Card> getAllPotal(){
        return cardRepository.findAll();
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

    public Card getMetaDataByYoutAPI(String videoId) {
        Card videoMetaData = null;
        try {
            // YouTube Data API 요청 URL 생성
            String apiUrl = "https://www.googleapis.com/youtube/v3/videos?id=" + videoId +
                    "&key=" + "AIzaSyBw5QDUFVZgn4wYMPRtSNkrXcpuUswsJqo" +
                    "&part=snippet" +
                    "&fields=items(snippet(title,thumbnails,channelTitle))";

            // HTTP 클라이언트 생성
            HttpClient client = HttpClient.newHttpClient();

            // HTTP 요청 생성
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            // HTTP 요청 및 응답 처리
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // JSON 데이터 파싱
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(response.body(), JsonObject.class);

            // items 배열에서 첫 번째 아이템을 가져옴
            JsonArray items = json.getAsJsonArray("items");

            if (items != null && items.size() > 0) {
                JsonElement firstItem = items.get(0);
                JsonObject snippet = firstItem.getAsJsonObject().getAsJsonObject("snippet");

                // 필요한 데이터 추출
                String title = snippet.getAsJsonPrimitive("title").getAsString();
                String channelTitle = snippet.getAsJsonPrimitive("channelTitle").getAsString();

                JsonObject thumbnails = snippet.getAsJsonObject("thumbnails");
                // 여기서 필요한 썸네일 크기 선택
                String thumbnailUrl = thumbnails.getAsJsonObject("medium").getAsJsonPrimitive("url").getAsString();

                // Potalgate 객체 생성
                videoMetaData = new Card();

                videoMetaData.setCardYoutId(videoId);
                System.out.println(videoMetaData.getCardYoutId()+" #");
                videoMetaData.setCardPlaylistIdx(3);
                System.out.println("playlist의 구현이 있기 전까지 test playlist에 저장됨");r

                videoMetaData.setCardYoutId(title);
                System.out.println(videoMetaData.getCardYoutTitle()+" #");

                videoMetaData.setCardYoutChannerName(channelTitle);
                System.out.println(videoMetaData.getCardYoutChannerName()+" #");

                videoMetaData.setCardYoutThumNail(thumbnailUrl);
                System.out.println(videoMetaData.getCardYoutThumNail()+" #");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return videoMetaData;
    }

    public void saveVideoMetaData(CardDto videoMetaData) {
        try{
            cardRepository.save(videoMetaData);
        }catch (Exception e){
            System.out.println(e);
        }

        //DB 저장 JPA로 구현

    }
}
