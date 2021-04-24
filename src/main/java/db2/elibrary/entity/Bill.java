package db2.elibrary.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    private String outTradeNo;
    private String tradeNo;
    private Double amount;
    private String code;
    private String msg;
    private String payStatus;
    private Timestamp payTime;

}
