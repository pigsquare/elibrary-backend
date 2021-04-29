package db2.elibrary.repository;

import db2.elibrary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmailOrTel(String s1, String s2, String s3);
    Optional<User> findByTel(String tel);
    Optional<User> findByCardNo(String cardNo);
}
