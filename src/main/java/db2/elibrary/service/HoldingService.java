package db2.elibrary.service;

import db2.elibrary.dto.HoldingAddRequestDto;
import db2.elibrary.dto.HoldingInfoResponseDto;

import java.util.List;

public interface HoldingService {
    String getBarcode(String isbn);
    Boolean addHolding(HoldingAddRequestDto requestDto);
    List<HoldingInfoResponseDto> getHoldingsByIsbn(String isbn);
}
