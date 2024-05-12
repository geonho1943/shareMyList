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
        playlist.setPlaylistStatus(true);
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
            cardRepository.deleteAllByCardPlaylistIdx(playlistIdx);
            playlistRepository.deleteByPlaylistIdx(playlistIdx);
        }else {
            throw new Exception("리스트를 변경할수 없습니다");
        }
    }


    public void deactivatePlaylist(List<PlaylistDto> deleteListinfo) {
        //playlist 비활성화
        List<Playlist> deactivatePlaylist = new ArrayList<>();

        // deleteListinfo에서 각 PlaylistDto 객체를 순회
        for (PlaylistDto playlistDto : deleteListinfo) {
            Playlist playlist = new Playlist(
                    playlistDto.getPlaylistIdx(),playlistDto.getPlaylistUserIdx(),
                    playlistDto.getPlaylistName(),false);
            deactivatePlaylist.add(playlist);
            // 업데이트된 Playlist를 리스트에 추가
        }
        playlistRepository.saveAll(deactivatePlaylist);
    }
}
