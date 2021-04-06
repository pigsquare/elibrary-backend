package db2.elibrary.entity;

import lombok.Data;
import org.springframework.data.annotation.Reference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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

    private String adminId;
}
