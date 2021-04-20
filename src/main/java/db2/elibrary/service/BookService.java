package db2.elibrary.service;

import db2.elibrary.dto.UpdateBookRequestDto;
import db2.elibrary.entity.Book;

import java.util.List;

public interface BookService {
    Book addBook(UpdateBookRequestDto requestDto);
    Book updateBook(UpdateBookRequestDto requestDto);
    Boolean delBook(String isbn);
    Book getBook(String isbn);
    List<Book> getBooks();
}
