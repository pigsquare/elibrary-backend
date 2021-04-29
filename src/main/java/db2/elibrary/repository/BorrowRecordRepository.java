package db2.elibrary.repository;

import db2.elibrary.entity.BorrowRecord;
import db2.elibrary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {
    Integer countBorrowRecordsByUserAndReturnTimeIsNull(User user);
    List<BorrowRecord> findByBook_BarcodeAndReturnTimeIsNullOrderByBorrowTimeDesc(String barcode);
    Long countByUserAndReturnTimeIsNull(User user);
    List<BorrowRecord> findByUser_IdOrderByBorrowTimeDesc(String userId);
    List<BorrowRecord> findByUser_IdAndReturnTimeIsNullOrderByBorrowTimeDesc(String userId);
}
