package db2.elibrary.controller;

import db2.elibrary.dto.BorrowBookRequestDto;
import db2.elibrary.dto.BorrowRecordResponseDto;
import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.service.BorrowRecordService;
import db2.elibrary.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowRestController {
    private BorrowRecordService borrowRecordService;

    @Autowired
    public BorrowRestController(BorrowRecordService borrowRecordService){ this.borrowRecordService = borrowRecordService; }

    // TODO: 借书
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

    // TODO: 还书
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

    // TODO: 续借
    @PostMapping("/renew/{recordId}")
    public CommonResponseDto renewBook(@PathVariable String recordId){
        return null;
    }

    // TODO: 获取当前借书列表
    @GetMapping("/list")
    public List<BorrowRecordResponseDto> getList(){
        return null;
    }

    // TODO: 获取当前未还的借书列表
    @GetMapping("/list/borrowing")
    public List<BorrowRecordResponseDto> getBorrowingList(){
        return null;
    }
}
