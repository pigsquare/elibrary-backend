package db2.elibrary.repository;

import db2.elibrary.entity.BorrowRecord;
import db2.elibrary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {
    Long countByUserAndReturnTimeIsNull(User user);
}
