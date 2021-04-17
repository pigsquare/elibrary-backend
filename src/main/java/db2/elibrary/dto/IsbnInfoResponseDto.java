package db2.elibrary.dto;

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
}
