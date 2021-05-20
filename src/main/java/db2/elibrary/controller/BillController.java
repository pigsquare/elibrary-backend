package db2.elibrary.controller;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.pay.CompensationRequestDto;
import db2.elibrary.dto.pay.PayRequestDto;
import db2.elibrary.dto.user.UserProfileResponseDto;
import db2.elibrary.service.BillService;
import db2.elibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/bill")
public class BillController {
    private final UserService userService;
    private final BillService billService;

    @Autowired
    public BillController(UserService userService, BillService billService){
        this.userService = userService;
        this.billService = billService;
    }
    @GetMapping("/balance/{cardNo}")
    public CommonResponseDto getBalance(@PathVariable String cardNo){
        CommonResponseDto commonResponseDto = new CommonResponseDto();
        commonResponseDto.setArgs(new UserProfileResponseDto(userService.getBalance(cardNo)));
        commonResponseDto.setMessage("获取成功");
        return commonResponseDto;
    }

    // 缴纳违约金
    @PostMapping("/pay/balance")
    public CommonResponseDto payOwe(@RequestBody @Valid PayRequestDto requestDto){
        return billService.payOwe(requestDto);
    }

    // 赔书
    @PostMapping("/compensations")
    public CommonResponseDto compensateBook(@RequestBody @Valid CompensationRequestDto requestDto){
        return billService.compensateBook(requestDto);
    }

    // 充值
    @PostMapping("/pay/recharge")
    public CommonResponseDto recharge(@RequestBody @Valid PayRequestDto requestDto){
        return billService.recharge(requestDto);
    }
}
