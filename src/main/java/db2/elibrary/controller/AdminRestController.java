package db2.elibrary.controller;

import db2.elibrary.dto.CommonResponseDto;
import db2.elibrary.dto.admin.AddAdminRequestDto;
import db2.elibrary.dto.admin.AdminInfoResponseDto;
import db2.elibrary.entity.Admin;
import db2.elibrary.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminRestController {
    private final AdminService adminService;

    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 增加管理员
    @PostMapping("/")
    public Admin addAdmin(@RequestBody @Valid AddAdminRequestDto addAdminRequestDto){
        return adminService.addAdmin(addAdminRequestDto.getTel(),addAdminRequestDto.getSalary(),addAdminRequestDto.getTitle());
    }

    // 更新管理员
    @PutMapping("/")
    public Admin updateAdmin(@RequestBody @Valid AddAdminRequestDto addAdminRequestDto){
        return adminService.updateAdmin(addAdminRequestDto.getTel(),addAdminRequestDto.getSalary(),addAdminRequestDto.getTitle());
    }

    // 查询所有管理员信息
    @GetMapping("/")
    public List<AdminInfoResponseDto> getAllAdmins(){
        return adminService.getAdmins();
    }

    // 查询单个管理员信息
    @GetMapping("/{tel}")
    public AdminInfoResponseDto getAdmin(@PathVariable String tel){
        return adminService.getAdmin(tel);
    }

    // 删除管理员
    @DeleteMapping("/{tel}")
    public Boolean delAdmin(@PathVariable String tel){
        return adminService.delAdmin(tel);
    }
}
