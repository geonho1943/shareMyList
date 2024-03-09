package com.geonho1943.sharemylist.repository;

import com.geonho1943.sharemylist.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAll();

    Card getAllByCardIdx(int cardIdx);

    List<Card> getAllByCardPlaylistIdx(int playlistIdx);

    void deleteAllByCardPlaylistIdx(int playlistIdx);

    void deleteByCardIdx(int cardIdx);
}
