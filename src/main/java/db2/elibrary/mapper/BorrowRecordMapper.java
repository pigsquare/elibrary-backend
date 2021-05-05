package db2.elibrary.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
@Mapper
public interface BorrowRecordMapper {
    Integer vocationRenewal(Date start, Date finish);
}
