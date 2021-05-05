package db2.elibrary.controller;

import db2.elibrary.dto.*;
import db2.elibrary.dto.auth.AuthTokenRequestDto;
import db2.elibrary.dto.auth.AuthTokenResponseDto;
import db2.elibrary.dto.auth.RegisterByTelDto;
import db2.elibrary.dto.auth.ValidateByTelRequestDto;
import db2.elibrary.exception.AuthException;
import db2.elibrary.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthRestController {

    private AuthService authService;

    @Autowired
    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthTokenResponseDto login(@RequestBody @Valid AuthTokenRequestDto requestDto){
        log.info("login");
        return authService.login(requestDto);
    }
    @PostMapping("/register/tel")
    public CommonResponseDto registerByTel(@Valid @RequestBody RegisterByTelDto dto){
        dto.addPrefix();
        CommonResponseDto responseDto = new CommonResponseDto();
        responseDto.setMessage(authService.registerByTel(dto.getTel()));
        return responseDto;
    }
    @PostMapping("/validate/tel")
    public AuthTokenResponseDto validateTel(@Valid @RequestBody ValidateByTelRequestDto dto)throws AuthException {
        dto.addPrefix();
        if(authService.validateTel(dto.getPrefixTel(),dto.getCode(),dto.getPassword())){
            return authService.registerByTelSuccess(dto);
        }
        else throw new AuthException("验证码不正确");
    }
    @RequestMapping("/validate/email/{token}")
    public AuthTokenResponseDto validateEmail(@PathVariable String token){
        return authService.validateEmail(token);
    }
    @RequestMapping("/refresh/{token}")
    public AuthTokenResponseDto updateToken(@PathVariable String token){
        return authService.refreshToken(token);
    }
}
