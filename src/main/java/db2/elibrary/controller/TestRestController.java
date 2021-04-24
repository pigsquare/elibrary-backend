package db2.elibrary.controller;

import com.alipay.api.AlipayApiException;
import db2.elibrary.dto.AddUserTestDto;
import db2.elibrary.dto.IsbnInfoResponseDto;
import db2.elibrary.entity.User;
import db2.elibrary.service.AuthService;
import db2.elibrary.service.HoldingService;
import db2.elibrary.service.MailService;
import db2.elibrary.service.SmsService;
import db2.elibrary.util.AliPayUtil;
import db2.elibrary.util.crawler.HttpUtilDownPage;
import org.checkerframework.checker.units.qual.A;
import org.htmlcleaner.XPatherException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("test")
public class TestRestController{
    @Autowired
    private AuthService authService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private MailService mailService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private HttpUtilDownPage httpUtilDownPage;
    @Autowired
    private HoldingService holdingService;
    @PostMapping("/adduser")
    public User AddUser(@Valid @RequestBody AddUserTestDto addUserTestDto){
        return authService.TestRegister(addUserTestDto);
    }
    @GetMapping("/sendsms")
    public String testSms(){
        // mailService.sendTextMail("zhufz3528ui@126.com","测试邮件","图书管理系统-进行测试\n普通邮件测试~",mailSender);
        return smsService.sendReservationSuccessSms("","《计算机体系结构》","3月28日",7,"4月10日");
    }
    @RequestMapping("/alipay/{barcode}")
    public String testAlipay(@PathVariable String barcode, @RequestParam String fee) throws AlipayApiException {
        AliPayUtil aliPayUtil = new AliPayUtil();
        String  res = "";
        Double p = Double.parseDouble(fee);
        try {
            res = aliPayUtil.payFee(p,barcode,"test");
        }catch (AlipayApiException e){
            e.printStackTrace();
        }
        return res;
    }
    @RequestMapping("/book/info/{isbn}")
    public IsbnInfoResponseDto getBookInfo(@PathVariable String isbn) throws XPatherException {
        return httpUtilDownPage.parseBookInfo(isbn);
    }
    @RequestMapping("/isbn/barcode/{isbn}")
    public String getBarcodeByIsbn(@PathVariable String isbn){
        return holdingService.getBarcode(isbn);
    }

}
