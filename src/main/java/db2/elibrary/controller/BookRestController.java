package db2.elibrary.controller;

import db2.elibrary.dto.book.BookInfoResponseDto;
import db2.elibrary.dto.book.UpdateBookRequestDto;
import db2.elibrary.entity.Book;
import db2.elibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookRestController {
    private BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    // 获取全部图书的列表
    @GetMapping("/")
    public List<BookInfoResponseDto> getAllBooks(){
        return bookService.getBooks();
    }
    // 添加图书
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/")
    public Book addBook(@RequestBody @Valid UpdateBookRequestDto requestDto){
        return bookService.addBook(requestDto);
    }
    // 修改图书信息
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/{isbn}")
    public Book updateBook(@PathVariable String isbn, @RequestBody @Valid UpdateBookRequestDto requestDto){
        return bookService.updateBook(requestDto);
    }
    // 删除图书
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @DeleteMapping("/{isbn}")
    public Boolean delBook(@PathVariable String isbn){
        return bookService.delBook(isbn);
    }
}
