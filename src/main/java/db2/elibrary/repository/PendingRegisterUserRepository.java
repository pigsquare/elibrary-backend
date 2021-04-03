package db2.elibrary.repository;

import db2.elibrary.entity.PendingRegisterUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingRegisterUserRepository extends JpaRepository<PendingRegisterUser, Long> {
    Optional<PendingRegisterUser> findByPhone(String phone);
}
