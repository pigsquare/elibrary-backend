package db2.elibrary.controller;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.pay.CompensationRequestDto;
import db2.elibrary.dto.pay.PayRequestDto;
import db2.elibrary.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/bill")
public class BillController {
    private UserService userService;
    public BillController(UserService userService){this.userService = userService;}
    @GetMapping("/balance/{tel}")
    public CommonResponseDto getBalance(@PathVariable String tel){
        CommonResponseDto commonResponseDto = new CommonResponseDto();
        commonResponseDto.setArgs(userService.getBalance("+86"+tel));
        commonResponseDto.setMessage("获取成功");
        return commonResponseDto;
    }

    // TODO: 缴纳违约金
    @PostMapping("/pay/balance")
    public CommonResponseDto payOwe(@RequestBody @Valid PayRequestDto requestDto){
        return null;
    }

    // TODO: 赔书
    @PostMapping("/compensations")
    public CommonResponseDto compensateBook(@RequestBody @Valid CompensationRequestDto requestDto){
        // 1. 如果赔书，args返回新的条码号
        // 2. 需要修改借阅记录为已还并备注，修改对应的holding的状态（LOST或者新的条码号）
        return null;
    }

    // TODO: 充值
}
