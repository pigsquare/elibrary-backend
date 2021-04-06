package db2.elibrary.dto;

import lombok.Data;

@Data
public class CommonResponseDto {
    private Integer code = 200;
    private String message;
    private Object args;
}
