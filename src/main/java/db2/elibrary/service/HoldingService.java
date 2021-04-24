package db2.elibrary.service;

import db2.elibrary.dto.HoldingAddRequestDto;

public interface HoldingService {
    String getBarcode(String isbn);
    Boolean addHolding(HoldingAddRequestDto requestDto);
}
