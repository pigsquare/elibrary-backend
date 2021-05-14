package db2.elibrary.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class AddAdminRequestDto {
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[1][3-9][0-9]{9}$")
    String tel;
    Double salary;
    String title;
}
