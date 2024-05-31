package com.geonho1943.sharemylist.service;

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
        return playlistToCheck.getPlaylistUserIdx() == userIdx;
    }

    @Transactional
    public void deletePlaylist(int playlistIdx, int userIdx) throws Exception {
        //플레이리스트 삭제
        Playlist playlistCreatedByUser = playlistRepository.findPlaylistUseridxByPlaylistIdx(playlistIdx);
        if (playlistCreatedByUser.getPlaylistUserIdx() == userIdx){
            cardRepository.deleteAllByCardPlaylistIdx(playlistIdx);
            playlistRepository.deleteByPlaylistIdx(playlistIdx);
        }else {
            throw new Exception("unableModifyPlaylist");
        }
    }


    public void deactivatePlaylist(List<PlaylistDto> deleteListinfo) {
        //playlist 비활성화
        List<Playlist> deactivatePlaylist = new ArrayList<>();

        // deleteListinfo에서 각 PlaylistDto 객체를 순회하며 status를 비활성화하도록 수정
        for (PlaylistDto playlistDto : deleteListinfo) {
            Playlist playlist = new Playlist(playlistDto);
            playlist.setPlaylistStatus(false);
            deactivatePlaylist.add(playlist);
        }
        playlistRepository.saveAll(deactivatePlaylist);
    }
}
