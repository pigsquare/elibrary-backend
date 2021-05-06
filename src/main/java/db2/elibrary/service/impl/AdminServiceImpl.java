package db2.elibrary.service.impl;

import db2.elibrary.dto.admin.AdminInfoResponseDto;
import db2.elibrary.entity.Admin;
import db2.elibrary.entity.User;
import db2.elibrary.exception.NotFoundException;
import db2.elibrary.repository.AdminRepository;
import db2.elibrary.repository.UserRepository;
import db2.elibrary.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public AdminServiceImpl(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Admin addAdmin(String tel, Double salary, String title) {
        if(tel!=null){
            tel = "+86" + tel;
        }
        Optional<User> optionalUser = userRepository.findByTel(tel);
        if(optionalUser.isEmpty()){
            throw new NotFoundException("");
        }
        Admin newAdmin = new Admin();
        newAdmin.setUser(optionalUser.get());
        newAdmin.setSalary(salary);
        newAdmin.setTitle(title);
        return adminRepository.save(newAdmin);
    }

    @Override
    public Admin updateAdmin(String tel, Double salary, String title) {
        if(tel!=null) {
            tel = "+86" + tel;
        }
        Optional<Admin> optionalAdmin = adminRepository.findByUser_Tel(tel);
        if(optionalAdmin.isEmpty()){
            throw new NotFoundException("");
        }
        Admin admin = optionalAdmin.get();
        admin.setSalary(salary);
        admin.setTitle(title);
        return adminRepository.save(admin);
    }

    @Override
    public List<AdminInfoResponseDto> getAdmins() {
        List<Admin> adminList = adminRepository.findAll();
        List<AdminInfoResponseDto> adminInfoResponseDtoList = new ArrayList<>();
        for(Admin admin:adminList){
            adminInfoResponseDtoList.add(new AdminInfoResponseDto(admin));
        }
        return adminInfoResponseDtoList;
    }

    @Override
    public AdminInfoResponseDto getAdmin(String tel) {
        if(tel!=null) {
            tel = "+86" + tel;
        }
        Optional<Admin> optionalAdmin = adminRepository.findByUser_Tel(tel);
        if(optionalAdmin.isEmpty()){
            throw new NotFoundException("");
        }
        return new AdminInfoResponseDto(optionalAdmin.get());
    }

    @Override
    public Boolean delAdmin(String tel) {
        if(tel!=null) {
            tel = "+86" + tel;
        }
        adminRepository.deleteByUser_Tel(tel);
        return true;
    }
}
