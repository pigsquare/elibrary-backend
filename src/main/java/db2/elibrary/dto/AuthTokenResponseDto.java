package db2.elibrary.dto;

import db2.elibrary.entity.enums.RoleEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AuthTokenResponseDto {
    private String token;
    private String username;
    private Long expiration;
    private String name;
    private String id;
    private RoleEnum role;
}
