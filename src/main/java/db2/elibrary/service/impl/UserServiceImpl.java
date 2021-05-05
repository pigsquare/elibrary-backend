package db2.elibrary.service.impl;

import db2.elibrary.entity.Grade;
import db2.elibrary.entity.User;
import db2.elibrary.exception.NotFoundException;
import db2.elibrary.repository.GradeRepository;
import db2.elibrary.repository.UserRepository;
import db2.elibrary.service.MailService;
import db2.elibrary.service.UserService;
import db2.elibrary.util.JwtUtil;
import db2.elibrary.util.UserUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.*;

@Service("user_service")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MailService mailService;
    private final JwtUtil jwtUtil;
    private final FreeMarkerConfigurer freeMarkerConfigurer;
    private final JavaMailSender mailSender;
    private final GradeRepository gradeRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MailService mailService, JwtUtil jwtUtil, FreeMarkerConfigurer freeMarkerConfigurer, JavaMailSender mailSender, GradeRepository gradeRepository) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.jwtUtil = jwtUtil;
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        this.mailSender = mailSender;
        this.gradeRepository = gradeRepository;
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
    public Boolean sendMailVerify(String mailAddr) throws IOException, TemplateException {
        // 部署地址
        String tempUrl = "http://localhost:4200/verify/";
        String userId = UserUtil.getCurrentUserAccount();
        if(userId == null){
            throw new NotFoundException("");
        }
        User user = userRepository.getOne(userId);
        String jwt = jwtUtil.generateEmailValidationToken(user, mailAddr);
        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("name", user.getName());
        mailModel.put("url", tempUrl + jwt);
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        mailModel.put("year", year);
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("activation_email.xhtml");
        String mailText = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModel);
        mailService.sendHtmlMail(mailAddr, "No reply - Validate your email account", mailText, mailSender);
        return true;
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

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Boolean updateUsername(String username) {
        Optional<User> userOptional = userRepository.findById(Objects.requireNonNull(UserUtil.getCurrentUserAccount()));
        if(userOptional.isEmpty()){
            throw new NotFoundException("");
        }
        User user = userOptional.get();
        user.setUsername(username);
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean updateGrade(String userId, Integer gradeId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new NotFoundException("");
        }
        User user = userOptional.get();
        Optional<Grade> gradeOptional = gradeRepository.findById(gradeId);
        if(gradeOptional.isEmpty()){
            throw new NotFoundException("");
        }
        Grade grade = gradeOptional.get();
        user.setGrade(grade);
        userRepository.save(user);
        return true;
    }
}
