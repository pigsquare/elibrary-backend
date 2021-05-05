package db2.elibrary.service;

import db2.elibrary.dto.auth.AddUserTestDto;
import db2.elibrary.dto.auth.AuthTokenRequestDto;
import db2.elibrary.dto.auth.AuthTokenResponseDto;
import db2.elibrary.dto.auth.ValidateByTelRequestDto;
import db2.elibrary.entity.User;
import db2.elibrary.exception.AuthException;

public interface AuthService {
    AuthTokenResponseDto login(AuthTokenRequestDto requestDto) throws AuthException;
    User TestRegister(AddUserTestDto addUserTestDto) throws AuthException;
    // Boolean registerByEmail();
    String registerByTel(String tel);
    Boolean validateTel(String tel, String code, String password);
    AuthTokenResponseDto registerByTelSuccess(ValidateByTelRequestDto dto);
    AuthTokenResponseDto validateEmail(String token);
    AuthTokenResponseDto refreshToken(String token);
}
