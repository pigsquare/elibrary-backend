package db2.elibrary.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PayRequestDto {
    @NotNull
    @NotBlank
    private String barcode;
    @NotNull
    @NotBlank
    private String tel;
    private Double total;
}
