package db2.elibrary.repository;

import db2.elibrary.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    List<Bill> findByUserIdOrderByPayTimeDesc(String id);
}
