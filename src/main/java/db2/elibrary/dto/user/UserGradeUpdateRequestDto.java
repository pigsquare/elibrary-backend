package db2.elibrary.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserGradeUpdateRequestDto {
    @NotNull
    @NotBlank
    private String userId;
    @NotNull
    @NotBlank
    private Integer gradeId;
}
