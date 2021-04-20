package db2.elibrary.service.impl;

import db2.elibrary.dto.UpdateBookRequestDto;
import db2.elibrary.entity.Admin;
import db2.elibrary.entity.Book;
import db2.elibrary.exception.AuthException;
import db2.elibrary.repository.AdminRepository;
import db2.elibrary.repository.BookRepository;
import db2.elibrary.service.BookService;
import db2.elibrary.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;
    private AdminRepository adminRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AdminRepository adminRepository) {
        this.bookRepository = bookRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public Book addBook(UpdateBookRequestDto requestDto) {
        if(bookRepository.existsById(requestDto.getIsbn())){
            throw new AuthException("err");
        }
        Book book = new Book();
        book.setBookInfo(requestDto);
        String uuid = UserUtil.getCurrentUserAccount();
        Optional<Admin> optionalAdmin = adminRepository.findByUserId(uuid);
        optionalAdmin.ifPresent(book::setAdmin);
        bookRepository.save(book);
        return book;
    }

    @Override
    public Book updateBook(UpdateBookRequestDto requestDto) {
        Book book = new Book();
        book.setBookInfo(requestDto);
        Optional<Admin> optionalAdmin = adminRepository.findByUserId(UserUtil.getCurrentUserAccount());
        optionalAdmin.ifPresent(book::setAdmin);
        bookRepository.save(book);
        return book;
    }

    @Override
    public Boolean delBook(String isbn) {
        bookRepository.deleteById(isbn);
        return true;
    }

    @Override
    public Book getBook(String isbn) {
        return bookRepository.getOne(isbn);
    }

    @Override
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }
}
