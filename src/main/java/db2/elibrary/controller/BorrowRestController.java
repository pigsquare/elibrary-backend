package db2.elibrary.controller;

import db2.elibrary.dto.BorrowBookRequestDto;
import db2.elibrary.dto.BorrowRecordResponseDto;
import db2.elibrary.dto.CommonResponseDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowRestController {

    // TODO: 借书
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/borrow")
    public CommonResponseDto borrowBook(@RequestBody BorrowBookRequestDto requestDto){
        return null;
    }

    // TODO: 还书
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/return/{barcode}")
    public CommonResponseDto returnBook(@PathVariable String barcode){
        return null;
    }

    // TODO: 续借
    @PostMapping("/renew/{bookId}")
    public CommonResponseDto renewBook(@PathVariable String bookId){
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
