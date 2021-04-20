package db2.elibrary.controller;

import db2.elibrary.dto.IsbnInfoResponseDto;
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

    @Autowired
    public PublicSearchController(HttpUtilDownPage httpUtilDownPage) {
        this.httpUtilDownPage = httpUtilDownPage;
    }

    @RequestMapping("/book/info/{isbn}")
    public IsbnInfoResponseDto getBookInfo(@PathVariable String isbn) throws XPatherException {
        return httpUtilDownPage.parseBookInfo(isbn);
    }
}
