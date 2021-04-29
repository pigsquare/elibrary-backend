package db2.elibrary.service;

import db2.elibrary.entity.User;
import db2.elibrary.exception.NotFoundException;
import org.apache.xpath.operations.Bool;

import java.util.List;

public interface UserService {
    Boolean changePassword(String oldPassword, String newPassword);
//    Boolean updateUsername(String username);
//    User updateUserProfile();
//    Boolean sendMailVerify(String mailAddr);
//    Boolean unableUser(String userInfo);
//    String  delUser(String id);
    User getProfile();
//    Boolean verifyEmail(String token);
//    Boolean prepay(Double amount);
    List<User> getAll();
    Boolean UpdateCardNo(String tel,String cardNo);
    Double getBalance(String tel) throws NotFoundException;
}
