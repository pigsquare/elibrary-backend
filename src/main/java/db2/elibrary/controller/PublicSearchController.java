package db2.elibrary.controller;

import db2.elibrary.dto.IsbnInfoResponseDto;
import db2.elibrary.service.BookService;
import db2.elibrary.util.crawler.HttpUtilDownPage;
import org.htmlcleaner.XPatherException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class PublicSearchController {
    private HttpUtilDownPage httpUtilDownPage;
    private BookService bookService;

    @Autowired
    public PublicSearchController(HttpUtilDownPage httpUtilDownPage, BookService bookService) {
        this.httpUtilDownPage = httpUtilDownPage;
        this.bookService = bookService;
    }

    @RequestMapping("/book/info/crawler/{isbn}")
    public IsbnInfoResponseDto getBookInfoByCrawler(@PathVariable String isbn) throws XPatherException {
        return httpUtilDownPage.parseBookInfo(isbn);
    }

    @RequestMapping("/book/info/{isbn}")
    public IsbnInfoResponseDto getBookInfo(@PathVariable String isbn){
        return bookService.getBook(isbn);
    }
}
