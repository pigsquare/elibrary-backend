package db2.elibrary.dto.holding;

import db2.elibrary.entity.Holding;
import lombok.Data;

@Data
public class HoldingInfoResponseDto {
    private Integer id;
    private String isbn;
    private String status;
    private String barcode;

    public HoldingInfoResponseDto(Holding holding){
        this.id = holding.getId();
        this.isbn = holding.getBook().getIsbn();
        this.barcode = holding.getBarcode();
        this.status = holding.getStatus().name();
    }
}
