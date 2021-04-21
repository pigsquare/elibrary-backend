package db2.elibrary.dto;

import db2.elibrary.entity.Book;
import lombok.Data;

@Data
public class IsbnInfoResponseDto {
    private String isbn;
    private String name;
    private String author;
    private String publisher;
    private String publishDate;
    private Double price;
    private String description;
    private String keywords;
    private String classifyCode;
    private String indexNo;
    private String pageInfo;
    private String imgUrl;

    public void phraseFromBook(Book book){
        this.setIsbn(book.getIsbn());
        this.setName(book.getName());
        this.setAuthor(book.getAuthor());
        this.setPublisher(book.getPublisher());
        this.setPublishDate(book.getPublishDate());
        this.setPrice(book.getPrice());
        this.setDescription(book.getDescription());
        this.setKeywords(book.getKeywords());
        this.setClassifyCode(book.getClassifyCode());
        this.setIndexNo(book.getIndexNo());
        this.setPageInfo(book.getPageInfo());
        this.setImgUrl(book.getImgUrl());
    }
}
