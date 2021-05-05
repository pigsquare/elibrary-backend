package db2.elibrary.service;

import db2.elibrary.dto.holding.HoldingAddRequestDto;
import db2.elibrary.dto.holding.HoldingInfoResponseDto;
import db2.elibrary.entity.Holding;
import db2.elibrary.entity.enums.BookStatusEnum;

import java.util.List;

public interface HoldingService {
    String getBarcode(String isbn);
    Boolean addHolding(HoldingAddRequestDto requestDto);
    List<HoldingInfoResponseDto> getHoldingsByIsbn(String isbn);
    Holding getHoldingByBarcode(String barcode);
    Boolean updateHolding(String barcode,String status);
}
