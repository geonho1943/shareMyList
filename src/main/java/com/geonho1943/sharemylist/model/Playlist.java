package com.geonho1943.sharemylist.model;

import com.geonho1943.sharemylist.dto.PlaylistDto;
import jakarta.persistence.*;

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

    @Column(name = "playlist_status", nullable = false)
    private Boolean playlistStatus;

    public Playlist() {}

    public Playlist(int playlistUserIdx, String playlistName) {
        this.playlistUserIdx = playlistUserIdx;
        this.playlistName = playlistName;
    }

    public Playlist(PlaylistDto playlistDto) {
        this.playlistIdx = playlistDto.getPlaylistIdx();
        this.playlistUserIdx = playlistDto.getPlaylistUserIdx();
        this.playlistName = playlistDto.getPlaylistName();
        this.playlistStatus = playlistDto.getPlaylistStatus();
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

    public Boolean getPlaylistStatus() {
        return playlistStatus;
    }

    public void setPlaylistStatus(Boolean playlistStatus) {
        this.playlistStatus = playlistStatus;
    }
}
