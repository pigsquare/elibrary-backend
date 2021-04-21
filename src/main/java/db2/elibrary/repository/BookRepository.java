package db2.elibrary.repository;

import db2.elibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {
    Long countByClassifyCodeIs(String code);
    List<Book> findByNameLike(String name);
    List<Book> findByAuthorLike(String name);
    List<Book> findByNameLikeOrAuthorLikeOrKeywordsLikeOrIndexNoLikeOrDescriptionLike(
            String s1, String s2, String s3, String s4, String s5
    );
}
