package db2.elibrary.controller;

import db2.elibrary.dto.book.BookInfoResponseDto;
import db2.elibrary.dto.book.BookSearchRequestDto;
import db2.elibrary.dto.holding.HoldingInfoResponseDto;
import db2.elibrary.dto.book.IsbnInfoResponseDto;
import db2.elibrary.service.BookService;
import db2.elibrary.service.HoldingService;
import db2.elibrary.util.crawler.HttpUtilDownPage;
import org.htmlcleaner.XPatherException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/search")
public class PublicSearchController {
    private HttpUtilDownPage httpUtilDownPage;
    private BookService bookService;
    private HoldingService holdingService;

    @Autowired
    public PublicSearchController(HttpUtilDownPage httpUtilDownPage, BookService bookService, HoldingService holdingService) {
        this.httpUtilDownPage = httpUtilDownPage;
        this.bookService = bookService;
        this.holdingService = holdingService;
    }
    // 获取爬虫爬取的指定图书的信息
    @RequestMapping("/crawler/books/{isbn}")
    public IsbnInfoResponseDto getBookInfoByCrawler(@PathVariable String isbn) throws XPatherException {
        return httpUtilDownPage.parseBookInfo(isbn);
    }
    // 获取数据库保存的指定图书的信息
    @RequestMapping("/books/{isbn}")
    public IsbnInfoResponseDto getBookInfo(@PathVariable String isbn){
        return bookService.getBook(isbn);
    }
    // 获取满足搜索条件的图书列表
    @RequestMapping("/books")
    public List<BookInfoResponseDto> searchBook(@RequestBody @Valid BookSearchRequestDto searchRequestDto){
        return bookService.searchBooks(searchRequestDto);
    }
    // 获取指定图书对应的藏书列表
    @GetMapping("/books/{isbn}/holdings")
    public List<HoldingInfoResponseDto> getInfoByIsbn(@PathVariable String isbn){
        return holdingService.getHoldingsByIsbn(isbn);
    }
}
