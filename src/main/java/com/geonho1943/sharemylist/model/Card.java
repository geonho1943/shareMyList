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

    @ManyToOne
    @JoinColumn(name = "playlist", nullable = false)
    private Playlist playlist;

    @Column(name = "card_yout_id")
    private String cardYoutId;

    @Column(name = "card_yout_title")
    private String cardYoutTitle;

    @Column(name = "card_yout_thumbnail_url")
    private String cardYoutThumNail;

    @Column(name = "card_yout_channername")
    private String cardYoutChannerName;

    @Column(name = "card_yout_description", length = 1000)
    private String cardYoutDescription;

    @Column(name = "card_yout_reg_data")
    private LocalDateTime cardYoutRegData;

    @Column(name = "card_status")
    private boolean cardStatus;

    public Card() {}

    public Card(CardDto cardDto) {
        this.cardIdx = cardDto.getCardIdx();
        this.cardYoutId = cardDto.getCardYoutId();
        this.cardYoutTitle = cardDto.getCardYoutTitle();
        this.cardYoutThumNail = cardDto.getCardYoutThumNail();
        this.cardYoutChannerName = cardDto.getCardYoutChannerName();
        this.cardYoutDescription = cardDto.getCardYoutDescription();
        this.cardYoutRegData = cardDto.getCardYoutRegData();
        this.playlist = cardDto.getPlaylist();
    }

    public int getCardIdx() {
        return cardIdx;
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

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

}
