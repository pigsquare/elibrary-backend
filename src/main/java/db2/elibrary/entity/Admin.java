package db2.elibrary.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    private User user;

    private String title;
    private Double salary;
}
