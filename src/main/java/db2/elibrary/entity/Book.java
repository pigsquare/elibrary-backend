package db2.elibrary.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import db2.elibrary.dto.UpdateBookRequestDto;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
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
    @JsonIgnore
    private Admin admin;

    public void setBookInfo(UpdateBookRequestDto requestDto){
        this.isbn = requestDto.getIsbn();
        this.name = requestDto.getName();
        this.author = requestDto.getAuthor();
        this.publisher = requestDto.getPublisher();
        this.publishDate = requestDto.getPublishDate();
        this.price = requestDto.getPrice();
        this.description = requestDto.getDescription();
        this.keywords = requestDto.getKeywords();
        this.classifyCode = requestDto.getClassifyCode();
        this.indexNo = requestDto.getIndexNo();
        this.pageInfo = requestDto.getPageInfo();
        this.imgUrl = requestDto.getImgUrl();
    }
}
