package db2.elibrary.service.impl;

import db2.elibrary.dto.auth.AddUserTestDto;
import db2.elibrary.dto.auth.AuthTokenRequestDto;
import db2.elibrary.dto.auth.AuthTokenResponseDto;
import db2.elibrary.dto.auth.ValidateByTelRequestDto;
import db2.elibrary.entity.Grade;
import db2.elibrary.entity.PendingRegisterUser;
import db2.elibrary.entity.User;
import db2.elibrary.entity.enums.RoleEnum;
import db2.elibrary.exception.AuthException;
import db2.elibrary.exception.NotFoundException;
import db2.elibrary.repository.GradeRepository;
import db2.elibrary.repository.PendingRegisterUserRepository;
import db2.elibrary.repository.UserRepository;
import db2.elibrary.service.AuthService;
import db2.elibrary.service.SmsService;
import db2.elibrary.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PendingRegisterUserRepository pendingRegisterUserRepository;
    private final GradeRepository gradeRepository;
    private final SmsService smsService;

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
    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager, PendingRegisterUserRepository pendingRegisterUserRepository, GradeRepository gradeRepository, SmsService smsService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.pendingRegisterUserRepository = pendingRegisterUserRepository;
        this.gradeRepository = gradeRepository;
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
                newUser.setName("用户" + username);
                newUser.setUnencodedPassword(password);
                // 设置等级0
                Optional<Grade> gradeOptional = gradeRepository.findById(0);
                if(gradeOptional.isEmpty()){
                    throw new NotFoundException("未定义该等级");
                }
                newUser.setGrade(gradeOptional.get());
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

    @Override
    public AuthTokenResponseDto validateEmail(String token) {
        try {
            if(jwtUtil.validateToken(token)){
                Claims claims = jwtUtil.getAllClaimsFromToken(token);
                String id = claims.getSubject();
                User user = userRepository.getOne(id);
                user.setEmail(claims.get("email", String.class));
                userRepository.save(user);
                return generateTokenResponse(user);
            }
        } catch (Exception e){
            log.warn("Validate Email Failed");
            throw new AuthException("invalid");
        }

        return null;
    }

    @Override
    public AuthTokenResponseDto refreshToken(String token) {
        try {
            if(jwtUtil.validateToken(token)){
                Claims claims = jwtUtil.getAllClaimsFromToken(token);
                String id = claims.getSubject();
                User user = userRepository.getOne(id);
                return generateTokenResponse(user);
            }
        } catch (Exception e){
            log.warn("Refresh Token Error");
            throw new AuthException("invalid");
        }
        return null;
    }
}
