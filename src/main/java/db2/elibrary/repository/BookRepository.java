package db2.elibrary.repository;

import db2.elibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
    Long countByClassifyCodeIs(String code);
}
