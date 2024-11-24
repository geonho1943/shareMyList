package com.geonho1943.sharemylist.dto;

import com.geonho1943.sharemylist.model.Card;
import com.geonho1943.sharemylist.model.Playlist;

import java.time.LocalDateTime;

public class CardDto {

    private int cardIdx;
    private Playlist playlist;
    private String cardYoutId;
    private String cardYoutTitle;
    private String cardYoutThumNail;
    private String cardYoutChannerName;
    private String cardYoutDescription;
    private LocalDateTime cardYoutRegData;
    private boolean cardStatus;

    public CardDto(){}

    public CardDto(Card card) {
        this.cardIdx = card.getCardIdx();
        this.playlist = card.getPlaylist();
        this.cardYoutId = card.getCardYoutId();
        this.cardYoutTitle = card.getCardYoutTitle();
        this.cardYoutThumNail = card.getCardYoutThumNail();
        this.cardYoutChannerName = card.getCardYoutChannerName();
        this.cardYoutDescription = card.getCardYoutDescription();
        this.cardYoutRegData = card.getCardYoutRegData();
    }

    public int getCardIdx() {
        return cardIdx;
    }

    public String getCardYoutId() {
        return cardYoutId;
    }

    public void setCardYoutId(String cardYoutId) {
        this.cardYoutId = cardYoutId;
    }

    public String getCardYoutTitle() {
        return cardYoutTitle;
    }

    public void setCardYoutTitle(String cardYoutTitle) {
        this.cardYoutTitle = cardYoutTitle;
    }

    public String getCardYoutThumNail() {
        return cardYoutThumNail;
    }

    public void setCardYoutThumNail(String cardYoutThumNail) {
        this.cardYoutThumNail = cardYoutThumNail;
    }

    public String getCardYoutChannerName() {
        return cardYoutChannerName;
    }

    public void setCardYoutChannerName(String cardYoutChannerName) {
        this.cardYoutChannerName = cardYoutChannerName;
    }

    public String getCardYoutDescription() {
        return cardYoutDescription;
    }

    public void setCardYoutDescription(String cardYoutDescription) {
        this.cardYoutDescription = cardYoutDescription;
    }

    public LocalDateTime getCardYoutRegData() {
        return cardYoutRegData;
    }

    public void setCardYoutRegData(LocalDateTime cardYoutRegData) {
        this.cardYoutRegData = cardYoutRegData;
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
