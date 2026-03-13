package com.geonho1943.sharemylist.dto;

import com.geonho1943.sharemylist.model.Playlist;

public class PlaylistDto {
    private int playlistIdx;
    private String playlistName;
    private boolean playlistStatus;
    private int userIdx;

    public PlaylistDto() {}

    public PlaylistDto(Playlist playlist) {
        this.playlistIdx = playlist.getPlaylistIdx();
        this.userIdx = playlist.getUser().getUserIdx();
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

    public boolean getPlaylistStatus() {
        return playlistStatus;
    }

    public void setPlaylistStatus(boolean playlistStatus) {
        this.playlistStatus = playlistStatus;
    }

    public int getUserIdx() {
        return userIdx;
    }
}
