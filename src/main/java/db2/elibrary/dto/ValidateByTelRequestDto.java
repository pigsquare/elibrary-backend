package db2.elibrary.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ValidateByTelRequestDto {
    @NotNull
    @Pattern(regexp = "^[1][3-9][0-9]{9}$")
    private String tel;
    @NotNull
    @NotBlank
    private String code;
    @NotNull
    @NotBlank
    private String password;
    private String prefixTel;
    public void addPrefix(){
        this.prefixTel="+86"+this.tel;
    }
}
