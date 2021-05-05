package db2.elibrary.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LibraryCardRequestDto {
    @NotNull
    @NotBlank
    private String tel;
    @NotNull
    @NotBlank
    private String cardNo;
}
