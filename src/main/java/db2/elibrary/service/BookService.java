package db2.elibrary.service;

import db2.elibrary.entity.Book;

import java.util.List;

public interface BookService {
    Book addBook(Book book);
    Book updateBook(Book book);
    Boolean delBook();
    Book getBook(String isbn);
    List<Book> getBookByName();
}
