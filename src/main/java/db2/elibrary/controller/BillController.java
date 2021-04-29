package db2.elibrary.controller;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.PayRequestDto;
import db2.elibrary.service.UserService;
import db2.elibrary.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/bill")
public class BillController {
    private UserService userService;
    public BillController(UserService userService){this.userService = userService;}
    @GetMapping("/balance/{tel}")
    CommonResponseDto getBalance(@PathVariable String tel){
        CommonResponseDto commonResponseDto = new CommonResponseDto();
        commonResponseDto.setArgs(userService.getBalance("+86"+tel));
        commonResponseDto.setMessage("获取成功");
        return commonResponseDto;
    }

    // TODO: 缴纳违约金
    @PostMapping("/pay/balance")
    CommonResponseDto payOwe(@RequestBody @Valid PayRequestDto requestDto){
        return null;
    }
}
