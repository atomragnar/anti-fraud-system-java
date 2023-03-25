package antifraud.repository;

import antifraud.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;


@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("select c from Card c where c.number = ?1")
    Optional<Card> findByNumber(String cardNumber);

    @Query("select c from Card c where c.number = ?1 and c.locked = ?2")
    Optional<Card> findByNumberAndLocked(String number, boolean locked);

    @Transactional
    @Modifying
    @Query("delete from Card c where c.number = ?1 and c.locked = ?2")
    int deleteByNumberAndLocked(String number, boolean locked);


    @Query("select (count(c) > 0) from Card c where c.number = ?1 and c.locked = ?2")
    boolean existsByNumberAndLocked(String cardNumber, boolean locked);

    @Query("select (count(c) > 0) from Card c where c.locked = ?1")
    boolean existsByLocked(boolean locked);

    @Query("select c from Card c where c.locked = ?1 order by c.id")
    List<Card> findByLockedOrderByIdAsc(boolean locked);

    @Query("select (count(c) > 0) from Card c where c.number = ?1")
    boolean existsByNumber(String cardNumber);





}
