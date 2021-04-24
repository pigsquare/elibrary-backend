package db2.elibrary.dto;

import db2.elibrary.entity.Book;
import lombok.Data;

@Data
public class BookInfoResponseDto {
    private String isbn;
    private String name;
    private String author;
    private String publisher;
    private String imgUrl;
    private String indexNo;

    public BookInfoResponseDto(Book book){
        this.isbn = book.getIsbn();
        this.author = book.getAuthor();
        this.name = book.getName();
        this.imgUrl = book.getImgUrl();
        this.publisher = book.getPublisher();
        this.indexNo = book.getIndexNo();
    }
}
