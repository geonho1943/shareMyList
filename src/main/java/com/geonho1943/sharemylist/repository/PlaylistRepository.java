package com.geonho1943.sharemylist.repository;

import com.geonho1943.sharemylist.model.Playlist;
import com.geonho1943.sharemylist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> findByUserAndPlaylistStatusTrueOrderByPlaylistIdxDesc(User user);

    Optional<Playlist> findByPlaylistIdx(int playlistIdx);
}
