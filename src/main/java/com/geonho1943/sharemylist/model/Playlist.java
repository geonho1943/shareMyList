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

    @Column(name = "playlist_is_boolean")
    private boolean playlistIsBoolean;

    public int getPlaylistIdx() {
        return playlistIdx;
    }

    public int getPlaylistUserIdx() {
        return playlistUserIdx;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public boolean isPlaylistIsBoolean() {
        return playlistIsBoolean;
    }
}
