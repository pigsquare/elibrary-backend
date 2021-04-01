package db2.elibrary.service.impl;

import db2.elibrary.dto.AddUserTestDto;
import db2.elibrary.dto.AuthTokenRequestDto;
import db2.elibrary.dto.AuthTokenResponseDto;
import db2.elibrary.entity.User;
import db2.elibrary.entity.enums.RoleEnum;
import db2.elibrary.exception.AuthException;
import db2.elibrary.repository.UserRepository;
import db2.elibrary.service.AuthService;
import db2.elibrary.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager, FreeMarkerConfigurer freeMarkerConfigurer) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }

    @Override
    public AuthTokenResponseDto Login(AuthTokenRequestDto requestDto) throws AuthException {
        log.info(requestDto.toString());
        UsernamePasswordAuthenticationToken upToken=new UsernamePasswordAuthenticationToken(requestDto.getUsername(),requestDto.getPassword());
        try {
            log.info(upToken.toString());
            Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            String jwtToken = jwtUtil.generateToken(user);
            AuthTokenResponseDto responseDto = new AuthTokenResponseDto();
            responseDto.setName(user.getName());
            responseDto.setToken(jwtToken);
            responseDto.setExpiration(jwtUtil.getExpirationDateFromToken(jwtToken).getTime());
            responseDto.setId(user.getId());
            responseDto.setRole(user.getRole());
            responseDto.setUsername(user.getUsername());
            return responseDto;
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
}
