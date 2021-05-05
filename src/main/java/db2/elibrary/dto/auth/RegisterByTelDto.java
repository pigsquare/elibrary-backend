package db2.elibrary.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegisterByTelDto {
    @NotNull
    @Pattern(regexp = "^[1][3-9][0-9]{9}$")
    private String tel;
    public void addPrefix(){
        this.tel="+86"+this.tel;
    }
}
