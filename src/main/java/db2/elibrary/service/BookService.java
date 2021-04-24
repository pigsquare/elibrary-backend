package db2.elibrary.service;

import db2.elibrary.dto.BookInfoResponseDto;
import db2.elibrary.dto.BookSearchRequestDto;
import db2.elibrary.dto.IsbnInfoResponseDto;
import db2.elibrary.dto.UpdateBookRequestDto;
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
