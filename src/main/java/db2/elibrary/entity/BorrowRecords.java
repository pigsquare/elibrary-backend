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
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Holding book;

    @ManyToOne Admin agent;

    private Timestamp borrowTime;

    private Date lastReturnDate;

    private Boolean extend;

    private Timestamp returnTime;

    private Double lateFee;

    private String memo;
}
