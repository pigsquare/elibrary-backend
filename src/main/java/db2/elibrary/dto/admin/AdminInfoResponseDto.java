package db2.elibrary.dto.admin;

import db2.elibrary.entity.Admin;

public class AdminInfoResponseDto {
    Integer id;
    Double salary;
    String title;
    String tel;
    public AdminInfoResponseDto(Admin admin){
        this.id = admin.getId();
        this.salary = admin.getSalary();
        this.title = admin.getTitle();
        this.tel = admin.getUser().getTel();
    }
}
