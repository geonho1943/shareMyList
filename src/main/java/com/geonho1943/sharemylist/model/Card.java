package com.geonho1943.sharemylist.model;

import jakarta.persistence.*;

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
private String cardYoutRegData;

}
