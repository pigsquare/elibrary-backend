package db2.elibrary.service.impl;

import db2.elibrary.dto.AddUserTestDto;
import db2.elibrary.dto.AuthTokenRequestDto;
import db2.elibrary.dto.AuthTokenResponseDto;
import db2.elibrary.dto.ValidateByTelRequestDto;
import db2.elibrary.entity.PendingRegisterUser;
import db2.elibrary.entity.User;
import db2.elibrary.entity.enums.RoleEnum;
import db2.elibrary.exception.AuthException;
import db2.elibrary.repository.PendingRegisterUserRepository;
import db2.elibrary.repository.UserRepository;
import db2.elibrary.service.AuthService;
import db2.elibrary.service.SmsService;
import db2.elibrary.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    private FreeMarkerConfigurer freeMarkerConfigurer;
    private PendingRegisterUserRepository pendingRegisterUserRepository;
    private SmsService smsService;

    private AuthTokenResponseDto generateTokenResponse(User user){
        String jwtToken = jwtUtil.generateToken(user);
        AuthTokenResponseDto responseDto = new AuthTokenResponseDto();
        responseDto.setName(user.getName());
        responseDto.setToken(jwtToken);
        responseDto.setExpiration(jwtUtil.getExpirationDateFromToken(jwtToken).getTime());
        responseDto.setId(user.getId());
        responseDto.setRole(user.getRole());
        responseDto.setUsername(user.getUsername());
        return responseDto;
    }

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager, FreeMarkerConfigurer freeMarkerConfigurer, PendingRegisterUserRepository pendingRegisterUserRepository, SmsService smsService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        this.pendingRegisterUserRepository = pendingRegisterUserRepository;
        this.smsService = smsService;
    }

    @Override
    public AuthTokenResponseDto login(AuthTokenRequestDto requestDto) throws AuthException {
        log.info(requestDto.toString());
        UsernamePasswordAuthenticationToken upToken=new UsernamePasswordAuthenticationToken(requestDto.getUsername(),requestDto.getPassword());
        try {
            log.info(upToken.toString());
            Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            return generateTokenResponse(user);
//            String jwtToken = jwtUtil.generateToken(user);
//            AuthTokenResponseDto responseDto = new AuthTokenResponseDto();
//            responseDto.setName(user.getName());
//            responseDto.setToken(jwtToken);
//            responseDto.setExpiration(jwtUtil.getExpirationDateFromToken(jwtToken).getTime());
//            responseDto.setId(user.getId());
//            responseDto.setRole(user.getRole());
//            responseDto.setUsername(user.getUsername());
//            return responseDto;
        } catch (DisabledException e) {
            log.warn("forbidden");
            throw new AuthException("你的账号被禁止登录！");
        } catch (BadCredentialsException e) {
            log.warn("wrong info");
            throw new AuthException("用户名或密码错误！");
        }
    }

    @Override
    public User TestRegister(AddUserTestDto addUserTestDto) throws AuthException {
        User user = new User();
        user.setUsername(addUserTestDto.getUsername());
        user.setUnencodedPassword(addUserTestDto.getPassword());
        user.setRole(RoleEnum.ROLE_READER);
        user.setEnabled(true);
        log.info(user.toString());
        userRepository.save(user);
        return user;
    }

    @Override
    public Boolean registerByEmail() {
        return null;
    }

    @Override
    public String registerByTel(String tel) {
        var optionalUser=userRepository.findByTel(tel);
        if(optionalUser.isPresent()){
            return "该号码已经注册！";
        }
        var optionalPendingRegisterUser = pendingRegisterUserRepository.findByPhone(tel);
        Date nowTime = new Date(System.currentTimeMillis());
        if(optionalPendingRegisterUser.isPresent()){
            var pendingRegisterUser = optionalPendingRegisterUser.get();
            var registerTime = pendingRegisterUser.getRegisterTime();
            Date canRegisterTime = new Date(registerTime.getTime()+60*1000);
            log.info(nowTime.getTime()+" vs "+canRegisterTime.getTime());
            if(nowTime.before(canRegisterTime))
                return "你在60秒内提交了注册请求，不能重复提交！";
            pendingRegisterUserRepository.delete(pendingRegisterUser);
        }
        PendingRegisterUser newUser = new PendingRegisterUser();
        newUser.setPhone(tel);
        newUser.setRegisterTime(new Timestamp(nowTime.getTime()));

        Random rand = new Random();
        int code = (rand.nextInt(900000)+100000);
        newUser.setCode(String.valueOf(code));
        pendingRegisterUserRepository.save(newUser);
        log.info("验证码已保存:"+code);
        smsService.sendVerifySms(tel,code+"");
        return "验证码已发送";
    }

    @Override
    public Boolean validateTel(String tel, String code, String password) {
        var pendingRegisterUser = pendingRegisterUserRepository.findByPhone(tel);
        if (pendingRegisterUser.isPresent()){
            if(code.equals(pendingRegisterUser.get().getCode())){
                String username = tel.substring(3);
                User newUser = new User();
                newUser.setTel(tel);
                newUser.setUsername(username);
                newUser.setUnencodedPassword(password);
                userRepository.save(newUser);
                pendingRegisterUserRepository.delete(pendingRegisterUser.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public AuthTokenResponseDto registerByTelSuccess(ValidateByTelRequestDto dto) {
        var optionalUser=userRepository.findByTel(dto.getPrefixTel());
        if(optionalUser.isPresent()){
            log.info("find user");
            User user=optionalUser.get();
            return generateTokenResponse(user);
        }

        return null;
    }
}
