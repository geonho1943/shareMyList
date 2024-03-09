package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.dto.CardDto;
import com.geonho1943.sharemylist.model.Card;
import com.geonho1943.sharemylist.repository.CardRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    @Value("${api.key}")
    private String apiKey;

    public List<CardDto> getAllCard() {
        List<Card> allCardEntity = cardRepository.findAll();
        List<CardDto> allCardDto = new ArrayList<>();

        for (Card card : allCardEntity) {
            allCardDto.add(new CardDto(card));
        }
        return allCardDto;
    }

    public String youtubeLinkParser(String youtubeLink) {
        String videoId = null;

        if (youtubeLink.startsWith("https://youtu.be/")) {
            // YouTube 단축 URL 형태 https://youtu.be/
            videoId = youtubeLink.substring("https://youtu.be/".length(), "https://youtu.be/".length() + 11);
        } else if (youtubeLink.startsWith("https://www.youtube.com/watch?v=")) {
            // YouTube 정식 URL 형태 https://www.youtube.com/watch?v=
            videoId = youtubeLink.substring("https://www.youtube.com/watch?v=".length(), "https://www.youtube.com/watch?v=".length() + 11);
        }
        return videoId;
    }

    public CardDto getMetaDataByYoutAPI(String videoId) {
        CardDto videoMetaData = null;
        try {
            // YouTube Data API 요청 URL 생성
            String apiUrl = "https://www.googleapis.com/youtube/v3/videos?id=" + videoId +
                    "&key=" + apiKey +
                    "&part=snippet" +
                    "&fields=items(snippet(title,channelTitle,publishedAt,thumbnails,description))";

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
                String publishedAt = snippet.getAsJsonPrimitive("publishedAt").getAsString();
                String description = snippet.getAsJsonPrimitive("description").getAsString();
                JsonObject thumbnails = snippet.getAsJsonObject("thumbnails");
                // 여기서 필요한 썸네일 크기 선택
                String thumbnailUrl = thumbnails.getAsJsonObject("medium").getAsJsonPrimitive("url").getAsString();

                // CardDto 객체 생성
                videoMetaData = new CardDto();
                videoMetaData.setCardYoutId(videoId);
                videoMetaData.setCardPlaylistIdx(3);
                videoMetaData.setCardYoutTitle(title);
                videoMetaData.setCardYoutChannerName(channelTitle);
                videoMetaData.setCardYoutDescription(description);
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                videoMetaData.setCardYoutRegData(LocalDateTime.parse(publishedAt, formatter));
                videoMetaData.setCardYoutThumNail(thumbnailUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return videoMetaData;
    }

    public void saveVideoMetaData(CardDto videoMetaData) {
        try{
            //DTO객체(videoMetaData)를 Entity(metaData)로 변경하여 save에 사용
            Card metaData = new Card(videoMetaData);
            cardRepository.save(metaData);
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public CardDto getCardInfo(int cardIdx) {
        //idx와 일치하는 card 정보를 반환
        return new CardDto(cardRepository.getAllByCardIdx(cardIdx));
    }

    public List<CardDto> getCardListByPlaylist(int playlistIdx) {
//        cardPlaylistIdx가 일치하는 필드를 list로 반환
        List<Card> cardInfoListEntity = cardRepository.getAllByCardPlaylistIdx(playlistIdx);
        List<CardDto> cardInfoList = new ArrayList<>();
        for (Card card : cardInfoListEntity) {
            cardInfoList.add(new CardDto(card));
        }
        return cardInfoList;
    }

    @Transactional
    public void deleteCard(int cardIdx) {
        cardRepository.deleteByCardIdx(cardIdx);

    }
}
