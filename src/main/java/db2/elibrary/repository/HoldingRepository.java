package db2.elibrary.repository;

import db2.elibrary.entity.Book;
import db2.elibrary.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<Holding, Integer> {
    Optional<Holding> findByBarcode(String barcode);
    List<Holding> findByBook(Book book);
}
