package db2.elibrary.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

    @ManyToOne
    private User admin;
}
