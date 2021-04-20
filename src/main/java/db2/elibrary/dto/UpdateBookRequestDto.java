package db2.elibrary.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateBookRequestDto {
    @NotNull
    @NotBlank
    private String isbn;
    @NotNull
    @NotBlank
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
