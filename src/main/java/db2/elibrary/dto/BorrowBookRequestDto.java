package db2.elibrary.dto;

import lombok.Data;

@Data
public class BorrowBookRequestDto {
    private String cardNo;
    private String barcode;
}
