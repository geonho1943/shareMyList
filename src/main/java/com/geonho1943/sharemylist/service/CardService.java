package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.dto.CardDto;
import com.geonho1943.sharemylist.model.Card;
import com.geonho1943.sharemylist.model.Playlist;
import com.geonho1943.sharemylist.repository.CardRepository;
import com.geonho1943.sharemylist.repository.PlaylistRepository;
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
    @Autowired
    private PlaylistRepository playlistRepository;

    @Value("${api.key}")
    private String apiKey;

    private List<CardDto> redefineCardList(List<Card> cardEntityList){
        List<CardDto> allCardDto = new ArrayList<>();
        for (int i = cardEntityList.size()-1; i >= 0; i--) {
            allCardDto.add(new CardDto(cardEntityList.get(i)));
        }
        return allCardDto;
    }

    public List<CardDto> getAllCard() {
        List<Card> allCardEntity = cardRepository.findByCardStatus(true);
        return redefineCardList(allCardEntity);
    }

    public CardDto getCardInfo(int cardIdx) {
        Card cardinfo = cardRepository.findByCardIdxAndCardStatus(cardIdx, true);
        return new CardDto(cardinfo);
    }

    public String youtubeLinkParser(String youtubeLink) {
        try {
            String videoId = null;
            if (youtubeLink.startsWith("https://youtu.be/")) {
                // YouTube 단축 URL 형태 https://youtu.be/
                videoId = youtubeLink.substring("https://youtu.be/".length(), "https://youtu.be/".length() + 11);
            } else if (youtubeLink.startsWith("https://www.youtube.com/watch?v=")) {
                // YouTube 정식 URL 형태 https://www.youtube.com/watch?v=
                videoId = youtubeLink.substring("https://www.youtube.com/watch?v=".length(), "https://www.youtube.com/watch?v=".length() + 11);
            }
            return videoId;
        }catch (NullPointerException e){
            throw new IllegalArgumentException("invalidYouTubeLink");
        }

    }

    public CardDto getMetaDataByYoutApi(String videoId) throws Exception {
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

            if (!items.isEmpty()) {
                JsonElement firstItem = items.get(0);
                JsonObject snippet = firstItem.getAsJsonObject().getAsJsonObject("snippet");

                String title = snippet.getAsJsonPrimitive("title").getAsString();
                String channelTitle = snippet.getAsJsonPrimitive("channelTitle").getAsString();
                String publishedAt = snippet.getAsJsonPrimitive("publishedAt").getAsString();
                String description = snippet.getAsJsonPrimitive("description").getAsString();
                JsonObject thumbnails = snippet.getAsJsonObject("thumbnails");
                String thumbnailUrl = thumbnails.getAsJsonObject("medium").getAsJsonPrimitive("url").getAsString();

                videoMetaData = new CardDto();
                videoMetaData.setCardYoutId(videoId);
                videoMetaData.setCardYoutTitle(title);
                videoMetaData.setCardYoutChannerName(channelTitle);
                videoMetaData.setCardYoutDescription(description);
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                videoMetaData.setCardYoutRegData(LocalDateTime.parse(publishedAt, formatter));
                videoMetaData.setCardYoutThumNail(thumbnailUrl);
            }
            return videoMetaData;
        } catch (IllegalArgumentException e) {
            throw new Exception("noVideoMetaDate");
        }
    }

    @Transactional
    public void saveCardToPlaylist(CardDto videoMetaData, int playlistIdx) {
        Playlist playlist = playlistRepository.findById((long) playlistIdx).orElseThrow(() -> new RuntimeException("Playlist not found"));
        Card metaData = new Card(videoMetaData);
        metaData.setCardStatus(true);
        metaData.setPlaylist(playlist);
        cardRepository.save(metaData);
    }

    @Transactional
    public List<CardDto> findCardsByPlaylist(int playlistIdx) {
        Playlist playlist = playlistRepository.findById((long) playlistIdx).orElseThrow(() -> new RuntimeException("Playlist not found"));
        List<Card> cardInfoListEntity = cardRepository.findAllByPlaylistAndCardStatus(playlist, true);
        return redefineCardList(cardInfoListEntity);
    }

    public void deactivateCard(int cardIdx) {
        Card card = cardRepository.findById((long) cardIdx)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        card.setCardStatus(false);
    }

    public List<CardDto> findAllCardByTitle(String keyword) {
        List<Card> cardEntityList = cardRepository.findAllByCardYoutTitleContaining(keyword);
        return redefineCardList(cardEntityList);
    }

}
