package com.geonho1943.sharemylist.repository;

import com.geonho1943.sharemylist.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

//    List<Playlist> findByPlaylistUserIdx(int playlistUserIdx);
    List<Playlist> findByPlaylistUserIdxAndPlaylistStatus(int playlistUserIdx, boolean playlistStatus);

    Playlist findPlaylistUseridxByPlaylistIdx(int cardPlaylistIdx);

    void deleteByPlaylistIdx(int playlistIdx);

}
