package db2.elibrary.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Book {
    @Id
    private String isbn;
    private String name;
    private String author;
    private String publisher;
    private String publishDate;
    private Double price;

    @Column(length = 1024)
    private String description;
    private String keywords;
    private String classifyCode;
    private String indexNo;
    private String pageInfo;
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Admin admin;
}
