package db2.elibrary.controller;

import db2.elibrary.dto.AuthTokenRequestDto;
import db2.elibrary.dto.AuthTokenResponseDto;
import db2.elibrary.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthRestController {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public AuthTokenResponseDto login(@RequestBody @Valid AuthTokenRequestDto requestDto){
        log.info("login");
        return authService.Login(requestDto);
    }
}
