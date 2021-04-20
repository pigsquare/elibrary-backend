package db2.elibrary.controller;

import db2.elibrary.dto.UpdateBookRequestDto;
import db2.elibrary.entity.Book;
import db2.elibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookRestController {
    private BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public List<Book> getAllBooks(){
        return bookService.getBooks();
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/add")
    public Book addBook(@RequestBody @Valid UpdateBookRequestDto requestDto){
        return bookService.addBook(requestDto);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/update")
    public Book updateBook(@RequestBody @Valid UpdateBookRequestDto requestDto){
        return bookService.updateBook(requestDto);
    }
}
