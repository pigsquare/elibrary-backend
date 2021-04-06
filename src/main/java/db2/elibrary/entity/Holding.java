package db2.elibrary.entity;

import db2.elibrary.entity.enums.StatusEnum;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Holding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Book book;

    private StatusEnum status;
    private String barcode;

    @ManyToOne
    private User admin;
}
