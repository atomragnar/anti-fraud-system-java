package antifraud.repository;

import antifraud.entities.SuspiciousIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SuspiciousIpRepository extends JpaRepository<SuspiciousIp, Long> {
    @Query("select (count(s) > 0) from SuspiciousIp s where s.ip = ?1")
    boolean existsByIp(String ip);
    @Query("select s from SuspiciousIp s where s.ip = ?1")
    Optional<SuspiciousIp> findByIp(String ip);
    @Query("select s from SuspiciousIp s order by s.id")
    List<SuspiciousIp> findByOrderByIdAsc();
    @Query("select (count(s) > 0) from SuspiciousIp s")
    boolean existsFirstBy();


}
