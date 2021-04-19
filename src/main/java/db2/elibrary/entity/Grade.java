package db2.elibrary.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String grade;
    private Integer maxHoldings;
    private Integer maxBorrowTime;
    private Integer maxReserveTime;
    private Integer creditNeeded;
}
