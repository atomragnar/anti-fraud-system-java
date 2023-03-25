package antifraud.repository;

import antifraud.common.TransactionResult;
import antifraud.entities.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    @Query("select t from Transaction t where t.number = ?1 and t.date between ?2 and ?3")
    List<Transaction> findByNumberAndDateBetween(String number, Timestamp dateStart, Timestamp dateEnd);


    @Query("select t from Transaction t where t.number = ?1 order by t.id")
    List<Transaction> findByNumberOrderByIdAsc(String number);

    List<Transaction> findByIdNotNullOrderByIdAsc();

    @Override
    boolean existsById(Long aLong);

    @Query("select (count(t) > 0) from Transaction t where t.number = ?1")
    boolean existsByNumber(String number);


    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Transaction t set t.feedback = ?1 where t.id = ?2")
    void updateFeedbackById(TransactionResult feedback, Long id);







}
