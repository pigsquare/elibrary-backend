package db2.elibrary.entity;

import db2.elibrary.entity.enums.BookStatusEnum;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class Holding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Book book;

    @Enumerated(EnumType.STRING)
    private BookStatusEnum status;

    @Column(unique = true)
    private String barcode;

    @ManyToOne
    private Admin admin;
}
