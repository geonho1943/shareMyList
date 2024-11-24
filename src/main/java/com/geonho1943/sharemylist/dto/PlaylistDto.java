package com.geonho1943.sharemylist.dto;

import com.geonho1943.sharemylist.model.Playlist;
import com.geonho1943.sharemylist.model.User;

public class PlaylistDto {
    private int playlistIdx;
    private String playlistName;
    private Boolean playlistStatus;
    private User user;

    public PlaylistDto() {}

    public PlaylistDto(Playlist playlist) {
        this.playlistIdx = playlist.getPlaylistIdx();
        this.user = playlist.getUser();
        this.playlistName = playlist.getPlaylistName();
        this.playlistStatus = playlist.getPlaylistStatus();
    }

    public int getPlaylistIdx() {
        return playlistIdx;
    }

    public void setPlaylistIdx(int playlistIdx) {
        this.playlistIdx = playlistIdx;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public Boolean getPlaylistStatus() {
        return playlistStatus;
    }

    public void setPlaylistStatus(Boolean playlistStatus) {
        this.playlistStatus = playlistStatus;
    }

    public User getUser() {
        return user;
    }
}
