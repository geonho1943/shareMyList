package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.dto.PlaylistDto;
import com.geonho1943.sharemylist.model.Playlist;
import com.geonho1943.sharemylist.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

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
}
