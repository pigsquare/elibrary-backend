package db2.elibrary.service;

import db2.elibrary.entity.User;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;

public interface UserService {
    Boolean changePassword(String oldPassword, String newPassword);
//    Boolean updateUsername(String username);
    void updateUser(User user);
    Boolean sendMailVerify(String mailAddr) throws IOException, TemplateException;
//    Boolean unableUser(String userInfo);
//    String  delUser(String id);
    User getProfile();
//    Boolean verifyEmail(String token);
//    Boolean prepay(Double amount);
    List<User> getAll();
    Boolean UpdateCardNo(String tel,String cardNo);
    User getBalance(String cardNo);
    User getUserByCardNo(String cardNo);
    Boolean updateUsername(String username);
    Boolean updateName(String name);
    Boolean updateGrade(String userId, Integer gradeId);
}
