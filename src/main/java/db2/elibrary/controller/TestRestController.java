package db2.elibrary.controller;

import db2.elibrary.dto.AddUserTestDto;
import db2.elibrary.entity.User;
import db2.elibrary.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("test")
public class TestRestController{
    @Autowired
    private AuthService authService;
    @PostMapping("/adduser")
    public User AddUser(@Valid @RequestBody AddUserTestDto addUserTestDto){
        return authService.TestRegister(addUserTestDto);
    }

}
