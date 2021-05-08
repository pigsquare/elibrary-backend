package db2.elibrary.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class MailAddRequestDto {
    @Email
    private String email;
}
