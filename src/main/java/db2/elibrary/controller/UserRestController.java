package db2.elibrary.controller;

import db2.elibrary.dto.*;
import db2.elibrary.entity.User;
import db2.elibrary.service.UserService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
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

    @GetMapping("/profile")
    public UserProfileResponseDto getProfile(){
        User user = userService.getProfile();
        return new UserProfileResponseDto(user);
    }

    // TODO: 生成并发送验证邮件
    @PostMapping("/update/mail")
    public CommonResponseDto submitEmail(@RequestBody MailAddRequestDto requestDto) throws IOException, TemplateException {
        CommonResponseDto responseDto = new CommonResponseDto();
        if(userService.sendMailVerify(requestDto.getEmail())){
            responseDto.setMessage("验证邮件发送成功，请至邮箱查收！");
        }
        return responseDto;
    }
}
