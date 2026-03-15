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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CardService {
    private static final int YOUTUBE_VIDEO_ID_LENGTH = 11;
    private static final Pattern SHORT_URL_PATTERN = Pattern.compile("^https://youtu\\.be/([A-Za-z0-9_-]{11}).*$");
    private static final Pattern WATCH_URL_PATTERN = Pattern.compile("[?&]v=([A-Za-z0-9_-]{11})");

    private final CardRepository cardRepository;
    private final PlaylistRepository playlistRepository;

    @Value("${api.key}")
    private String apiKey;

    public CardService(CardRepository cardRepository, PlaylistRepository playlistRepository) {
        this.cardRepository = cardRepository;
        this.playlistRepository = playlistRepository;
    }

    public List<CardDto> getAllCard() {
        return cardRepository.findAllByCardStatusTrueOrderByCardIdxDesc().stream()
                .map(CardDto::new)
                .toList();
    }

    public CardDto getCardInfo(int cardIdx) {
        return new CardDto(getActiveCard(cardIdx));
    }

    public String parseYoutubeVideoId(String youtubeLink) {
        if (youtubeLink == null || youtubeLink.isBlank()) {
            throw new IllegalArgumentException("invalidYouTubeLink");
        }

        Matcher shortUrlMatcher = SHORT_URL_PATTERN.matcher(youtubeLink);
        if (shortUrlMatcher.matches()) {
            return shortUrlMatcher.group(1);
        }

        if (youtubeLink.startsWith("https://www.youtube.com/watch")
                || youtubeLink.startsWith("https://youtube.com/watch")) {
            Matcher watchUrlMatcher = WATCH_URL_PATTERN.matcher(youtubeLink);
            if (watchUrlMatcher.find()) {
                return watchUrlMatcher.group(1);
            }
        }

        throw new IllegalArgumentException("invalidYouTubeLink");
    }

    public CardDto getMetadataByYoutubeApi(String videoId) {
        if (videoId == null || videoId.length() != YOUTUBE_VIDEO_ID_LENGTH) {
            throw new IllegalArgumentException("invalidYouTubeLink");
        }

        try {
            String apiUrl = "https://www.googleapis.com/youtube/v3/videos?id=" + videoId +
                    "&key=" + apiKey +
                    "&part=snippet" +
                    "&fields=items(snippet(title,channelTitle,publishedAt,thumbnails,description))";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            JsonObject json = new Gson().fromJson(response.body(), JsonObject.class);
            JsonArray items = json.getAsJsonArray("items");
            if (items == null || items.isEmpty()) {
                throw new IllegalArgumentException("noVideoMetaDate");
            }

            JsonElement firstItem = items.get(0);
            JsonObject snippet = firstItem.getAsJsonObject().getAsJsonObject("snippet");

            String title = snippet.getAsJsonPrimitive("title").getAsString();
            String channelTitle = snippet.getAsJsonPrimitive("channelTitle").getAsString();
            String publishedAt = snippet.getAsJsonPrimitive("publishedAt").getAsString();
            String description = snippet.getAsJsonPrimitive("description").getAsString();
            JsonObject thumbnails = snippet.getAsJsonObject("thumbnails");
            String thumbnailUrl = thumbnails.getAsJsonObject("medium").getAsJsonPrimitive("url").getAsString();

            CardDto videoMetaData = new CardDto();
            videoMetaData.setCardYoutId(videoId);
            videoMetaData.setCardYoutTitle(title);
            videoMetaData.setCardYoutChannerName(channelTitle);
            videoMetaData.setCardYoutDescription(description == null ? "" : description);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            videoMetaData.setCardYoutRegData(LocalDateTime.parse(publishedAt, formatter));
            videoMetaData.setCardYoutThumNail(thumbnailUrl);
            return videoMetaData;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("youtubeApiRequestFailed", e);
        }
    }

    @Transactional
    public void saveCardToPlaylist(CardDto videoMetaData, int playlistIdx, int userIdx) {
        Playlist playlist = getPlaylistById(playlistIdx);
        validatePlaylistOwnership(playlist, userIdx);
        Card metadata = new Card(videoMetaData, playlist);
        metadata.setCardStatus(true);
        cardRepository.save(metadata);
    }

    @Transactional
    public List<CardDto> findCardsByPlaylist(int playlistIdx) {
        Playlist playlist = getPlaylistById(playlistIdx);
        return cardRepository.findAllByPlaylistAndCardStatusTrueOrderByCardIdxDesc(playlist).stream()
                .map(CardDto::new)
                .toList();
    }

    @Transactional
    public void deactivateCard(int cardIdx) {
        Card card = getActiveCard(cardIdx);
        card.setCardStatus(false);
    }

    @Transactional
    public int deactivateCard(int cardIdx, int userIdx) {
        Card card = getActiveCard(cardIdx);
        validateCardOwnership(card, userIdx);
        card.setCardStatus(false);
        return card.getPlaylist().getPlaylistIdx();
    }

    public List<CardDto> findAllCardByTitle(String keyword) {
        return cardRepository.findAllByCardYoutTitleContainingAndCardStatusTrueOrderByCardIdxDesc(keyword).stream()
                .map(CardDto::new)
                .toList();
    }

    private Card getActiveCard(int cardIdx) {
        return cardRepository.findByCardIdxAndCardStatusTrue(cardIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드입니다."));
    }

    private Playlist getPlaylistById(int playlistIdx) {
        return playlistRepository.findByPlaylistIdx(playlistIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 플레이리스트입니다."));
    }

    private void validatePlaylistOwnership(Playlist playlist, int userIdx) {
        if (playlist.getUser().getUserIdx() != userIdx) {
            throw new IllegalArgumentException("userCheck");
        }
    }

    private void validateCardOwnership(Card card, int userIdx) {
        validatePlaylistOwnership(card.getPlaylist(), userIdx);
    }

}
