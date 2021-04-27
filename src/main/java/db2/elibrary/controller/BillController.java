package db2.elibrary.controller;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.PayRequestDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/bill")
public class BillController {
    // TODO: 获取用户账户余额
    @GetMapping("/balance/{tel}")
    CommonResponseDto getBalance(@PathVariable String tel){
        return null;
    }

    // TODO: 缴纳违约金
    @PostMapping("/pay/balance")
    CommonResponseDto payOwe(@RequestBody @Valid PayRequestDto requestDto){
        return null;
    }
}
