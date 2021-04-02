package db2.elibrary.controller;

import db2.elibrary.dto.AddUserTestDto;
import db2.elibrary.entity.User;
import db2.elibrary.service.AuthService;
import db2.elibrary.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("test")
public class TestRestController{
    @Autowired
    private AuthService authService;
    @Autowired
    private SmsService smsService;
    @PostMapping("/adduser")
    public User AddUser(@Valid @RequestBody AddUserTestDto addUserTestDto){
        return authService.TestRegister(addUserTestDto);
    }
    @GetMapping("/sendsms")
    public String testSms(){
        return smsService.sendReservationSuccessSms("","《计组2》","3月28日",7,"4月10日");
    }

}
