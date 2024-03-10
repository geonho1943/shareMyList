package com.geonho1943.sharemylist.dto;

import com.geonho1943.sharemylist.model.Playlist;

public class PlaylistDto {
    private int playlistIdx;
    private int playlistUserIdx;
    private String playlistName;
    private Boolean playlistIsBoolean;
    private Boolean playlistStatus;

    public PlaylistDto() {}

    public PlaylistDto(Playlist playlist) {
        //본인의 playlist 조회용 생성자
        this.playlistIdx = playlist.getPlaylistIdx();
        this.playlistUserIdx = playlist.getPlaylistUserIdx();
        this.playlistName = playlist.getPlaylistName();
        this.playlistIsBoolean = playlist.getPlaylistIsBoolean();
        this.playlistStatus = playlist.getPlaylistStatus();
    }

    public int getPlaylistIdx() {
        return playlistIdx;
    }

    public void setPlaylistIdx(int playlistIdx) {
        this.playlistIdx = playlistIdx;
    }

    public int getPlaylistUserIdx() {
        return playlistUserIdx;
    }

    public void setPlaylistUserIdx(int playlistUserIdx) {
        this.playlistUserIdx = playlistUserIdx;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public Boolean getPlaylistIsBoolean() {
        return playlistIsBoolean;
    }

    public void setPlaylistIsBoolean(Boolean playlistIsBoolean) {
        this.playlistIsBoolean = playlistIsBoolean;
    }

    public Boolean getPlaylistStatus() {
        return playlistStatus;
    }

    public void setPlaylistStatus(Boolean playlistStatus) {
        this.playlistStatus = playlistStatus;
    }
}
