package db2.elibrary.dto.pay;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CompensationRequestDto {
    @NotBlank
    @NotNull
    private String barcode; // ali-pay barcode
    @NotBlank
    @NotNull
    private String cardInfo;
    @NotNull
    private Integer borrowRecordId;

    private Double additionalFee;

    private Boolean isCompensateBook = false;   // 是否赔书
}
