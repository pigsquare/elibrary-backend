package db2.elibrary.service;

import db2.elibrary.dto.book.BookInfoResponseDto;
import db2.elibrary.dto.book.BookSearchRequestDto;
import db2.elibrary.dto.book.IsbnInfoResponseDto;
import db2.elibrary.dto.book.UpdateBookRequestDto;
import db2.elibrary.entity.Book;

import java.util.List;

public interface BookService {
    Book addBook(UpdateBookRequestDto requestDto);
    Book updateBook(UpdateBookRequestDto requestDto);
    Boolean delBook(String isbn);
    IsbnInfoResponseDto getBook(String isbn);
    List<BookInfoResponseDto> getBooks();
    List<BookInfoResponseDto> searchBooks(BookSearchRequestDto searchRequestDto);
}
