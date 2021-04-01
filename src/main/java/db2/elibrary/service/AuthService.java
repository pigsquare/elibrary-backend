package db2.elibrary.service;

import db2.elibrary.dto.AddUserTestDto;
import db2.elibrary.dto.AuthTokenRequestDto;
import db2.elibrary.dto.AuthTokenResponseDto;
import db2.elibrary.entity.User;
import db2.elibrary.exception.AuthException;

public interface AuthService {
    AuthTokenResponseDto Login(AuthTokenRequestDto requestDto) throws AuthException;
    User TestRegister(AddUserTestDto addUserTestDto) throws AuthException;
}
