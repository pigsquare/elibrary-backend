package db2.elibrary.service;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.pay.CompensationRequestDto;
import db2.elibrary.dto.pay.PayRequestDto;

public interface BillService {
    CommonResponseDto payOwe(PayRequestDto payRequestDto);
    CommonResponseDto recharge(PayRequestDto payRequestDto);
    CommonResponseDto compensateBook(CompensationRequestDto compensationRequestDto);
}
