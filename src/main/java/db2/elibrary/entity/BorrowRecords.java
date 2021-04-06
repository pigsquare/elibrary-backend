package db2.elibrary.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
public class BorrowRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Holding book;

    private Timestamp borrowTime;

    private Date lastReturnDate;

    private Boolean extend;

    private Timestamp returnTime;

    private String memo;
}
