package db2.elibrary.service.impl;

import db2.elibrary.entity.User;
import db2.elibrary.exception.AuthException;
import db2.elibrary.exception.NotFoundException;
import db2.elibrary.repository.UserRepository;
import db2.elibrary.service.UserService;
import db2.elibrary.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("user_service")
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Boolean changePassword(String oldPassword, String newPassword) {
        String id = UserUtil.getCurrentUserAccount();
        if(id!=null){
            User user = userRepository.getOne(id);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if(passwordEncoder.matches(oldPassword,user.getPassword())){
                user.setUnencodedPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }

        return false;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Boolean UpdateCardNo(String tel, String cardNo) {
        Optional<User> optionalUser = userRepository.findByTel(tel);
        if(optionalUser.isEmpty())
            return false;
        User user = optionalUser.get();
        user.setCardNo(cardNo);
        userRepository.save(user);
        return true;
    }

    @Override
    public Double getBalance(String tel) throws NotFoundException{
        Optional<User> optionalUser = userRepository.findByTel(tel);
        if(optionalUser.isEmpty()) {
            throw new NotFoundException("该手机号未注册！");
        }
        User user = optionalUser.get();
        return user.getBalance();
    }

    @Override
    public User getProfile() {
        Optional<User> optionalUser = userRepository.findById(Objects.requireNonNull(UserUtil.getCurrentUserAccount()));
        if(optionalUser.isEmpty()) {
            throw new NotFoundException("查询失败！");
        }
        return optionalUser.get();
    }

    @Override
    public User getUserByCardNo(String cardNo) {
        Optional<User> userOptional = userRepository.findByCardNo(cardNo);
        if(userOptional.isEmpty())
            throw new NotFoundException("");
        return userOptional.get();
    }
}
