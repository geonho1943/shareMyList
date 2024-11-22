package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.dto.CardDto;
import com.geonho1943.sharemylist.dto.PlaylistDto;
import com.geonho1943.sharemylist.model.Playlist;
import com.geonho1943.sharemylist.repository.CardRepository;
import com.geonho1943.sharemylist.repository.PlaylistRepository;
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
    private CardRepository cardRepository;
    @Autowired
    private CardService cardService;

    public List<PlaylistDto> getPlaylistByOneUser(int playlistUserIdx) {
        List<Playlist> allPlaylistEntityByOneUser = playlistRepository.findByPlaylistUserIdxAndPlaylistStatus(playlistUserIdx,true);
        List<PlaylistDto> allPlaylistDtoByOneUser = new ArrayList<>();
        for (int i = allPlaylistEntityByOneUser.size()-1; i >= 0; i--) {
            allPlaylistDtoByOneUser.add(new PlaylistDto(allPlaylistEntityByOneUser.get(i)));
        }
        return allPlaylistDtoByOneUser;
    }

    public void createPlaylist(int userIdx, String playlistName) {
        Playlist playlist = new Playlist(userIdx, playlistName);
        playlist.setPlaylistStatus(true);
        playlistRepository.save(playlist);
    }

    public boolean isValidateCard(int cardPlaylistIdx, int userIdx) {
        Playlist playlistToCheck = playlistRepository.findPlaylistUseridxByPlaylistIdx(cardPlaylistIdx);
        return playlistToCheck.getPlaylistUserIdx() == userIdx; //본인이면 true
    }

    @Transactional
    public void deactivatePlaylist(int playlistIdx, int userIdx) throws Exception {
        Playlist playlistCreatedByUser = playlistRepository.findPlaylistUseridxByPlaylistIdx(playlistIdx);
        if (playlistCreatedByUser.getPlaylistUserIdx() == userIdx){
            List<CardDto> cards = cardService.findCardsByPlaylist(playlistCreatedByUser.getPlaylistIdx());
            for (CardDto card : cards) {
                cardService.deactivateCard(card.getCardIdx());
            }
            playlistCreatedByUser.setPlaylistStatus(false);
            playlistRepository.save(playlistCreatedByUser);
        }else {
            throw new Exception("unableModifyPlaylist");
        }
    }

}
