package db2.elibrary.dto;

import lombok.Data;

@Data
public class CommonResponseDto {
    private Integer code;
    private String message;
    private Object args;
}
