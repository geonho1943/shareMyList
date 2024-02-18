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
        int temp = playlistUserIdx;
        List<Playlist> allPlaylistEntityByOneUser = playlistRepository.findByPlaylistUserIdx(temp);

        List<PlaylistDto> allPlaylistDtoByOneUser = new ArrayList<>();
        for (Playlist playlist : allPlaylistEntityByOneUser){
            allPlaylistDtoByOneUser.add(new PlaylistDto(playlist));
        }
        return allPlaylistDtoByOneUser;
    }

    public void createPlaylist(int userIdx, String playlistName) {
        Playlist playlist = new Playlist(userIdx, playlistName);
        try {
            playlistRepository.save(playlist);
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public int verifyposs(int cardPlaylistIdx) {
        Playlist temp = null;
        try {
            temp = playlistRepository.findPlaylistUseridxByPlaylistIdx(cardPlaylistIdx);
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        return temp.getPlaylistUserIdx();
    }

    @Transactional
    public void deletePlaylist(int playlistIdx, int userIdx) throws Exception {
        //플레이리스트 삭제
        Playlist playlistCreatedByUser = playlistRepository.findPlaylistUseridxByPlaylistIdx(playlistIdx);
        if (playlistCreatedByUser.getPlaylistUserIdx() == userIdx){
            playlistRepository.deleteByPlaylistIdx(playlistIdx);
            //플레이리스트삭제
            cardRepository.deleteAllByCardPlaylistIdx(playlistIdx);
            //해당 플레이리스트의 카드 일괄 삭제
        }else {
            //유저 본인의 생성물이 아닐경우
            throw new Exception("타인의 리스트를 변경할수 없습니다");
        }

    }
}
