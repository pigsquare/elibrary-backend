package db2.elibrary.dto.borrow;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class VacationRequestDto {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
