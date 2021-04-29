package db2.elibrary.controller;

import db2.elibrary.dto.BookInfoResponseDto;
import db2.elibrary.dto.BookSearchRequestDto;
import db2.elibrary.dto.HoldingInfoResponseDto;
import db2.elibrary.dto.IsbnInfoResponseDto;
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

    @RequestMapping("/book/info/crawler/{isbn}")
    public IsbnInfoResponseDto getBookInfoByCrawler(@PathVariable String isbn) throws XPatherException {
        return httpUtilDownPage.parseBookInfo(isbn);
    }

    @RequestMapping("/book/info/{isbn}")
    public IsbnInfoResponseDto getBookInfo(@PathVariable String isbn){
        return bookService.getBook(isbn);
    }

    @RequestMapping("/book/list")
    public List<BookInfoResponseDto> searchBook(@RequestBody @Valid BookSearchRequestDto searchRequestDto){
        return bookService.searchBooks(searchRequestDto);
    }

    @GetMapping("/holding/info/{isbn}")
    public List<HoldingInfoResponseDto> getInfoByIsbn(@PathVariable String isbn){
        return holdingService.getHoldingsByIsbn(isbn);
    }

}
