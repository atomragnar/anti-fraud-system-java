package antifraud.repository;

import antifraud.entities.StolenCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StolenCardRepository extends JpaRepository<StolenCard, Long> {
    @Query("select s from StolenCard s order by s.id")
    List<StolenCard> findByOrderByIdAsc();

    @Override
    void deleteById(Long aLong);

    @Query("select (count(s) > 0) from StolenCard s")
    boolean existsFirstBy();

    @Query("select s from StolenCard s where s.number = ?1")
    Optional<StolenCard> findByNumber(String number);

    @Query("select (count(s) > 0) from StolenCard s where s.number = ?1")
    boolean existsByNumber(String number);



}
