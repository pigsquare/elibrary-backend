package db2.elibrary.controller;

import db2.elibrary.dto.AuthTokenRequestDto;
import db2.elibrary.dto.AuthTokenResponseDto;
import db2.elibrary.dto.RegisterByTelDto;
import db2.elibrary.dto.ValidateByTelRequestDto;
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
    public String registerByTel(@Valid @RequestBody RegisterByTelDto dto){
        dto.addPrefix();
        return authService.registerByTel(dto.getTel());
    }
    @PostMapping("/validate/tel")
    public AuthTokenResponseDto validateTel(@Valid @RequestBody ValidateByTelRequestDto dto){
        dto.addPrefix();
        if(authService.validateTel(dto.getPrefixTel(),dto.getCode(),dto.getPassword())){
            return authService.registerByTelSuccess(dto);
        }
        return null;
    }
}
