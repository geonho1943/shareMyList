package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.dto.CardDto;
import com.geonho1943.sharemylist.dto.PlaylistDto;
import com.geonho1943.sharemylist.model.Playlist;
import com.geonho1943.sharemylist.model.User;
import com.geonho1943.sharemylist.repository.PlaylistRepository;
import com.geonho1943.sharemylist.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final CardService cardService;
    private final UserRepository userRepository;

    public PlaylistService(PlaylistRepository playlistRepository, CardService cardService, UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.cardService = cardService;
        this.userRepository = userRepository;
    }

    public List<PlaylistDto> getPlaylistByUser(int userIdx) {
        User user = getUserById(userIdx);
        List<Playlist> playlistsByUser = playlistRepository.findByUserAndPlaylistStatusTrueOrderByPlaylistIdxDesc(user);
        return playlistsByUser.stream()
                .map(PlaylistDto::new)
                .toList();
    }

    @Transactional
    public void createPlaylist(int userIdx, String playlistName) {
        User user = getUserById(userIdx);
        Playlist playlist = new Playlist();
        playlist.setPlaylistName(playlistName);
        playlist.setPlaylistStatus(true);
        playlist.setUser(user);
        playlistRepository.save(playlist);
    }

    public boolean isValidatePlaylist(int playlistIdx, int userIdx) {
        Playlist playlistToCheck = getPlaylistById(playlistIdx);
        return playlistToCheck.getUser().getUserIdx() == userIdx; //본인이면 true
    }

    @Transactional
    public void deactivatePlaylist(int playlistIdx, int userIdx) throws Exception {
        if (!isValidatePlaylist(playlistIdx, userIdx)) {
            throw new Exception("unableModifyPlaylist");
        }
        Playlist playlistCreatedByUser = getPlaylistById(playlistIdx);
        List<CardDto> cards = cardService.findCardsByPlaylist(playlistCreatedByUser.getPlaylistIdx());
        for (CardDto card : cards) {
            cardService.deactivateCard(card.getCardIdx());
        }
        playlistCreatedByUser.setPlaylistStatus(false);
    }

    private User getUserById(int userIdx) {
        return userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private Playlist getPlaylistById(int playlistIdx) {
        return playlistRepository.findByPlaylistIdx(playlistIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 플레이리스트입니다."));
    }

}
