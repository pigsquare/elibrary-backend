package db2.elibrary.controller;

import db2.elibrary.dto.ChangePasswordRequestDto;
import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.LibraryCardRequestDto;
import db2.elibrary.entity.User;
import db2.elibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserRestController {
    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> findAll(){
        return userService.getAll();
    }

    @PostMapping("/password/change")
    public CommonResponseDto changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto){
        CommonResponseDto responseDto = new CommonResponseDto();
        if(userService.changePassword(requestDto.getOldPassword(), requestDto.getNewPassword())){
            responseDto.setMessage("修改成功");
        }else {
            responseDto.setMessage("修改失败");
        }
        return responseDto;
    }

    @PostMapping("/card")
    public CommonResponseDto libraryCardProcess(@RequestBody @Valid LibraryCardRequestDto requestDto){
        CommonResponseDto responseDto = new CommonResponseDto();
        if(userService.UpdateCardNo("+86"+requestDto.getTel(), requestDto.getCardNo())){
            responseDto.setMessage("录入成功");
        }else{
            responseDto.setMessage("录入失败");
        }
        return responseDto;
    }
}
