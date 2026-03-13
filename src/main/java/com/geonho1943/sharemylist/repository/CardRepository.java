package com.geonho1943.sharemylist.repository;

import com.geonho1943.sharemylist.model.Card;
import com.geonho1943.sharemylist.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByCardStatusTrueOrderByCardIdxDesc();

    Optional<Card> findByCardIdxAndCardStatusTrue(int cardIdx);

    List<Card> findAllByPlaylistAndCardStatusTrueOrderByCardIdxDesc(Playlist playlist);

    List<Card> findAllByCardYoutTitleContainingAndCardStatusTrueOrderByCardIdxDesc(String cardTitle);
}
