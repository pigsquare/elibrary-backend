package db2.elibrary.dto.admin;

import db2.elibrary.entity.Admin;
import lombok.Data;

@Data
public class AdminInfoResponseDto {
    private Integer id;
    private Double salary;
    private String title;
    private String tel;
    private String name;
    public AdminInfoResponseDto(Admin admin){
        this.id = admin.getId();
        this.salary = admin.getSalary();
        this.title = admin.getTitle();
        this.tel = admin.getUser().getTel();
        this.name = admin.getUser().getName();
    }
}
