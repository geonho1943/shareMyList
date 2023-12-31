package com.geonho1943.sharemylist.dto;

import com.geonho1943.sharemylist.model.Card;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CardDto {

    private int cardIdx;
    private int cardPlaylistIdx;
    private String cardYoutId;
    private String cardYoutTitle;
    private String cardYoutThumNail;
    private String cardYoutChannerName;
    private String cardYoutDescription;
    private String cardYoutRegData;

    public CardDto(){}

    public CardDto(Card card) {
        this.cardIdx = card.getCardIdx();
        this.cardPlaylistIdx = card.getCardPlaylistIdx();
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

    public void setCardIdx(int cardIdx) {
        this.cardIdx = cardIdx;
    }

    public int getCardPlaylistIdx() {
        return cardPlaylistIdx;
    }

    public void setCardPlaylistIdx(int cardPlaylistIdx) {
        this.cardPlaylistIdx = cardPlaylistIdx;
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

    public String getCardYoutRegData() {
        return cardYoutRegData;
    }

    public void setCardYoutRegData(String cardYoutRegData) {
        this.cardYoutRegData = cardYoutRegData;
    }

}
