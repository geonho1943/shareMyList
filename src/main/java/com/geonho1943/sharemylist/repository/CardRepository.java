package com.geonho1943.sharemylist.repository;

import com.geonho1943.sharemylist.model.Card;
import com.geonho1943.sharemylist.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByCardStatus(boolean b);

    Card findByCardIdxAndCardStatus(int cardIdx, boolean b);

    List<Card> findAllByPlaylistAndCardStatus(Playlist playlist, boolean b);

    void deleteByCardIdx(int cardIdx);

    List<Card> findAllByCardYoutTitleContaining(String cardTitle);
}
