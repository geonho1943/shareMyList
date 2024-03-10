package com.geonho1943.sharemylist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Playlist {

    @Id
    @Column(name = "playlist_idx")
    private int playlistIdx;

    @Column(name = "playlist_useridx")
    private int playlistUserIdx;

    @Column(name = "playlist_name")
    private String playlistName;

    @Column(name = "playlist_status")
    private Boolean playlistStatus;

    @Column(name = "playlist_is_boolean")
    private Boolean playlistIsBoolean;


    public Playlist() {}

    public Playlist(int playlistUserIdx) {
        this.playlistUserIdx = playlistUserIdx;
    }

    public Playlist(int playlistUserIdx, String playlistName) {
        this.playlistUserIdx = playlistUserIdx;
        this.playlistName = playlistName;
    }

    public Playlist(int playlistIdx, int playlistUserIdx, String playlistName, Boolean playlistStatus, Boolean playlistIsBoolean) {
        this.playlistIdx = playlistIdx;
        this.playlistUserIdx = playlistUserIdx;
        this.playlistName = playlistName;
        this.playlistStatus = playlistStatus;
        this.playlistIsBoolean = playlistIsBoolean;
    }

    public int getPlaylistIdx() {
        return playlistIdx;
    }

    public int getPlaylistUserIdx() {
        return playlistUserIdx;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public Boolean getPlaylistIsBoolean() {
        return playlistIsBoolean;
    }

    public Boolean getPlaylistStatus() {
        return playlistStatus;
    }

}
