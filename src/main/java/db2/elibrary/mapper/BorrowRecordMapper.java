package db2.elibrary.mapper;

import db2.elibrary.entity.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Component
@Mapper
public interface BorrowRecordMapper {
    Integer vocationRenewal(Date start, Date finish);
    List<BorrowRecord> getAboutDueBorrowingList();
}
