package db2.elibrary.controller;

import db2.elibrary.dto.BorrowBookRequestDto;
import db2.elibrary.dto.BorrowRecordResponseDto;
import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.entity.BorrowRecord;
import db2.elibrary.service.BorrowRecordService;
import db2.elibrary.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowRestController {
    private final BorrowRecordService borrowRecordService;

    @Autowired
    public BorrowRestController(BorrowRecordService borrowRecordService){ this.borrowRecordService = borrowRecordService; }


    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/borrow")
    public CommonResponseDto borrowBook(@RequestBody BorrowBookRequestDto requestDto){
        CommonResponseDto commonResponseDto = new CommonResponseDto();
        if(borrowRecordService.borrowHolding(requestDto.getCardNo(),requestDto.getBarcode())){
            commonResponseDto.setMessage("借书成功！");
        } else{
            commonResponseDto.setMessage("借书失败！");
        }
        return commonResponseDto;
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/return/{barcode}")
    public CommonResponseDto returnBook(@PathVariable String barcode){
        CommonResponseDto commonResponseDto = new CommonResponseDto();
        if(borrowRecordService.returnHolding(barcode)){
            commonResponseDto.setMessage("还书成功！");
        } else{
            commonResponseDto.setMessage("还书失败！");
        }
        return commonResponseDto;
    }

    @PostMapping("/renew/{recordId}")
    public CommonResponseDto renewBook(@PathVariable Integer recordId){
        CommonResponseDto commonResponseDto = new CommonResponseDto();
        if(borrowRecordService.renewHolding(recordId)){
            commonResponseDto.setMessage("续借成功！");
        } else{
            commonResponseDto.setMessage("续借失败！");
        }
        return commonResponseDto;
    }

    @GetMapping("/list")
    public List<BorrowRecordResponseDto> getList(){
        List<BorrowRecordResponseDto> borrowRecordResponseDtoList = new ArrayList<>();
        List<BorrowRecord> borrowRecordList = borrowRecordService.getList();
        for(BorrowRecord borrowRecord:borrowRecordList){
            borrowRecordResponseDtoList.add(new BorrowRecordResponseDto(borrowRecord));
        }
        return borrowRecordResponseDtoList;
    }

    // TODO: 获取当前未还的借书列表
    @GetMapping("/list/borrowing")
    public List<BorrowRecordResponseDto> getBorrowingList(){
        return null;
    }
}
