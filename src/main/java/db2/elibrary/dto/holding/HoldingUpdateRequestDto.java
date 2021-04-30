package db2.elibrary.dto.holding;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class HoldingUpdateRequestDto {
    @NotNull
    @NotBlank
    private String barcode;

    private String status = "AVAILABLE";
}
