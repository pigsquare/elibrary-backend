package db2.elibrary.dto.borrow;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BorrowBookRequestDto {
    @NotNull
    @NotBlank
    private String cardNo;
    @NotNull
    @NotBlank
    private String barcode;
}
