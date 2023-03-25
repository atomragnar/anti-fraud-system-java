package antifraud.repository;

import antifraud.common.RoleEnum;
import antifraud.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameIgnoreCase(String username);
    @Transactional
    void deleteUsersByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByRole(RoleEnum role);
    @org.springframework.transaction.annotation.Transactional
    @Modifying
    @Query("update UserEntity u set u.role = ?1 where u.id = ?2")
    void updateRoleById(RoleEnum role, Long id);

    @org.springframework.transaction.annotation.Transactional
    @Modifying
    @Query("update UserEntity u set u.accountLocked = ?1 where u.id = ?2")
    void updateAccountLockedById(boolean accountLocked, Long id);


}
