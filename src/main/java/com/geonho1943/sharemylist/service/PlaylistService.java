package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.dto.CardDto;
import com.geonho1943.sharemylist.dto.PlaylistDto;
import com.geonho1943.sharemylist.model.Playlist;
import com.geonho1943.sharemylist.model.User;
import com.geonho1943.sharemylist.repository.PlaylistRepository;
import com.geonho1943.sharemylist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private CardService cardService;
    @Autowired
    private UserRepository userRepository;

    public List<PlaylistDto> getPlaylistByUser(int userIdx) {
        User user = userRepository.findByUserIdx(userIdx);
        List<Playlist> playlistsByUser = playlistRepository.findByUserAndPlaylistStatus(user,true);
        List<PlaylistDto> playlistsDtoByUser = new ArrayList<>();
        for (int i = playlistsByUser.size()-1; i >= 0; i--) {
            playlistsDtoByUser.add(new PlaylistDto(playlistsByUser.get(i)));
        }
        return playlistsDtoByUser;
    }

    public void createPlaylist(int userIdx, String playlistName) {
        User user = userRepository.findByUserIdx(userIdx);
        Playlist playlist = new Playlist();
        playlist.setPlaylistName(playlistName);
        playlist.setPlaylistStatus(true);
        playlist.setUser(user);
        playlistRepository.save(playlist);
    }

    public boolean isValidatePlaylist(int playlistIdx, int userIdx) {
        Playlist playlistToCheck = playlistRepository.findByPlaylistIdx(playlistIdx);
        return playlistToCheck.getUser().getUserIdx() == userIdx; //본인이면 true
    }

    @Transactional
    public void deactivatePlaylist(int playlistIdx, int userIdx) throws Exception {
        if (!isValidatePlaylist(playlistIdx, userIdx)) {
            throw new Exception("unableModifyPlaylist");
        }
        Playlist playlistCreatedByUser = playlistRepository.findByPlaylistIdx(playlistIdx);
        List<CardDto> cards = cardService.findCardsByPlaylist(playlistCreatedByUser.getPlaylistIdx());
        for (CardDto card : cards) {
            cardService.deactivateCard(card.getCardIdx());
        }
        playlistCreatedByUser.setPlaylistStatus(false);
        playlistRepository.save(playlistCreatedByUser);
    }

}
