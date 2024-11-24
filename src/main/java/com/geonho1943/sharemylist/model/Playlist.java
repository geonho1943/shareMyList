package com.geonho1943.sharemylist.model;

import jakarta.persistence.*;

@Entity
@Table(name = "playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_idx")
    private int playlistIdx;

    @ManyToOne
    @JoinColumn(name = "users", nullable = false)
    private User user;

    @Column(name = "playlist_name")
    private String playlistName;

    @Column(name = "playlist_status", nullable = false)
    private Boolean playlistStatus;

    public Playlist() {}

    public int getPlaylistIdx() {
        return playlistIdx;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
}
