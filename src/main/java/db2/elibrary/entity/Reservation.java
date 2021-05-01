package db2.elibrary.entity;

import db2.elibrary.entity.enums.ReserveStatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Holding book;

    @ManyToOne
    private Book bookInfo;

    private Timestamp submitTime;

    private Date lastDate;

    private Boolean complete;

    @Enumerated(EnumType.STRING)
    private ReserveStatusEnum status;

    private String memo;
}
