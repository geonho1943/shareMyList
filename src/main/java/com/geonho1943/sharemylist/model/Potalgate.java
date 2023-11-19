package com.geonho1943.sharemylist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "potalgate")
public class Potalgate {
@Id
@Column(name = "potalgate_idx")
private int potalIdx;

@Column(name = "potalgate_playlist_idx")
private int potalPlaylistIdx;

@Column(name = "potalgate_yout_id")
private String potalYoutId;

@Column(name = "potalgate_yout_title")
private String potalYoutTitle;

@Column(name = "potalgate_yout_thumbnail_url")
private String potalYoutThumNail;

@Column(name = "potalgate_yout_channername")
private String potalYoutChannerName;

@Column(name = "potalgate_yout_description")
private String potalYoutDescription;

    public int getPotalIdx() {
        return potalIdx;
    }

    public int getPotalPlaylistIdx() {
        return potalPlaylistIdx;
    }

    public String getPotalYoutId() {
        return potalYoutId;
    }

    public String getPotalYoutTitle() {
        return potalYoutTitle;
    }

    public String getPotalYoutThumNail() {
        return potalYoutThumNail;
    }

    public String getPotalYoutChannerName() {
        return potalYoutChannerName;
    }

    public String getPotalYoutDescription() {
        return potalYoutDescription;
    }
}
