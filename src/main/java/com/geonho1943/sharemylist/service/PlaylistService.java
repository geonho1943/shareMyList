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

    public List<PlaylistDto> getPlaylistByOneUser(Long playlistUserIdx) {
        long temp = playlistUserIdx;
        List<Playlist> allPlaylistEntityByOneUser = playlistRepository.findByPlaylistUserIdx(temp);

        List<PlaylistDto> allPlaylistDtoByOneUser = new ArrayList<>();
        for (Playlist playlist : allPlaylistEntityByOneUser){
            allPlaylistDtoByOneUser.add(new PlaylistDto(playlist));
        }
        return allPlaylistDtoByOneUser;
    }
}
