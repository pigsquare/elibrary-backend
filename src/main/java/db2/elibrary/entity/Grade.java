package db2.elibrary.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
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
