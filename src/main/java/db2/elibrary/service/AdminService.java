package db2.elibrary.service;

import db2.elibrary.dto.admin.AdminInfoResponseDto;
import db2.elibrary.entity.Admin;

import java.util.List;

public interface AdminService {
    Admin addAdmin(String tel, Double salary, String title);
    Admin updateAdmin(String tel, Double salary, String title);
    List<AdminInfoResponseDto> getAdmins();
    AdminInfoResponseDto getAdmin(String tel);
    Boolean delAdmin(String tel);
}
