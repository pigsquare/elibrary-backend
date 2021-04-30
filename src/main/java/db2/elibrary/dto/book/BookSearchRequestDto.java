package db2.elibrary.dto.book;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BookSearchRequestDto {
    // 1--Name, 2--ISBN, 3--Author, 4--Fuzzy
    private Integer method = 4;

    @NotNull
    @NotBlank
    private String word;
}
