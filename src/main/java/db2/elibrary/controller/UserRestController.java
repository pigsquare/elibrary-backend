package db2.elibrary.controller;

import db2.elibrary.dto.*;
import db2.elibrary.dto.user.*;
import db2.elibrary.entity.User;
import db2.elibrary.service.UserService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestController {
    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }
    // 获取所有用户列表
    @GetMapping("/")
    public List<User> findAll() {
        return userService.getAll();
    }
    // 修改当前用户密码
    @PutMapping("/current-user/password")
    public CommonResponseDto changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto) {
        CommonResponseDto responseDto = new CommonResponseDto();
        if (userService.changePassword(requestDto.getOldPassword(), requestDto.getNewPassword())) {
            responseDto.setMessage("修改成功");
        } else {
            responseDto.setMessage("修改失败");
        }
        return responseDto;
    }
    // 办理借书卡
    @PostMapping("/current-user/card")
    public CommonResponseDto libraryCardProcess(@RequestBody @Valid LibraryCardRequestDto requestDto) {
        CommonResponseDto responseDto = new CommonResponseDto();
        if (userService.UpdateCardNo("+86" + requestDto.getTel(), requestDto.getCardNo())) {
            responseDto.setMessage("录入成功");
        } else {
            responseDto.setMessage("录入失败");
        }
        return responseDto;
    }
    // 获取当前用户信息
    @GetMapping("/current-user")
    public UserProfileResponseDto getProfile() {
        User user = userService.getProfile();
        return new UserProfileResponseDto(user);
    }

    // 生成并发送验证邮件
    @PostMapping("/validation")
    public CommonResponseDto submitEmail(@RequestBody @Valid MailAddRequestDto requestDto) throws IOException, TemplateException {
        CommonResponseDto responseDto = new CommonResponseDto();
        if (userService.sendMailVerify(requestDto.getEmail())) {
            responseDto.setMessage("验证邮件发送成功，请至邮箱查收！");
        }
        return responseDto;
    }
    // 更新用户名
    @PatchMapping("/username/{username}")
    public CommonResponseDto updateUsername(@PathVariable String username) {
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setArgs(userService.updateUsername(username));
        return responseDto;
    }
    // 更新姓名
    @PatchMapping("/name/{username}")
    public CommonResponseDto updateName(@PathVariable String username) {
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setArgs(userService.updateName(username));
        return responseDto;
    }
    // 更新用户等级
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PatchMapping("/grade")
    public CommonResponseDto updateGrade(@RequestBody UserGradeUpdateRequestDto requestDto) {
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setArgs(userService.updateGrade(requestDto.getUserId(),requestDto.getGradeId()));
        return responseDto;
    }
}
