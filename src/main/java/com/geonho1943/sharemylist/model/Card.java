package com.geonho1943.sharemylist.model;

import com.geonho1943.sharemylist.dto.CardDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "card")
public class Card {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "card_idx")
private int cardIdx;

@Column(name = "card_playlist_idx")
private int cardPlaylistIdx;

@Column(name = "card_yout_id")
private String cardYoutId;

@Column(name = "card_yout_title")
private String cardYoutTitle;

@Column(name = "card_yout_thumbnail_url")
private String cardYoutThumNail;

@Column(name = "card_yout_channername")
private String cardYoutChannerName;

@Column(name = "card_yout_description")
private String cardYoutDescription;

@Column(name = "card_yout_reg_data")
private LocalDateTime cardYoutRegData;

@Column(name = "card_status")
private boolean cardStatus;

    public Card() {}

    public Card(CardDto cardDto) {
        this.cardIdx = cardDto.getCardIdx();
        this.cardPlaylistIdx = cardDto.getCardPlaylistIdx();
        this.cardYoutId = cardDto.getCardYoutId();
        this.cardYoutTitle = cardDto.getCardYoutTitle();
        this.cardYoutThumNail = cardDto.getCardYoutThumNail();
        this.cardYoutChannerName = cardDto.getCardYoutChannerName();
        this.cardYoutDescription = cardDto.getCardYoutDescription();
        this.cardYoutRegData = cardDto.getCardYoutRegData();
    }

    public int getCardIdx() {
        return cardIdx;
    }

    public int getCardPlaylistIdx() {
        return cardPlaylistIdx;
    }

    public String getCardYoutId() {
        return cardYoutId;
    }

    public String getCardYoutTitle() {
        return cardYoutTitle;
    }

    public String getCardYoutThumNail() {
        return cardYoutThumNail;
    }

    public String getCardYoutChannerName() {
        return cardYoutChannerName;
    }

    public String getCardYoutDescription() {
        return cardYoutDescription;
    }

    public LocalDateTime getCardYoutRegData() {
        return cardYoutRegData;
    }

    public void setCardStatus(boolean cardStatus) {
        this.cardStatus = cardStatus;
    }
}
