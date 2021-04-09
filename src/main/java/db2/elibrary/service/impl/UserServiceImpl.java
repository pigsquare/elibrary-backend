package db2.elibrary.service.impl;

import db2.elibrary.entity.User;
import db2.elibrary.repository.UserRepository;
import db2.elibrary.service.UserService;
import db2.elibrary.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
