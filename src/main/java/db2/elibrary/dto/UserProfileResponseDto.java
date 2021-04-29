package db2.elibrary.dto;

import db2.elibrary.entity.User;
import lombok.Data;

@Data
public class UserProfileResponseDto {
    private Double balance;
    private String cardNo;
    private Integer credit;
    private String email;
    private String name;
    private String tel;
    private String username;
    private String grade;
    private Integer maxHoldings;
    private Integer maxBorrowTime;
    private Integer maxReserveTime;

    public UserProfileResponseDto(User user) {
        balance = user.getBalance();
        cardNo = user.getCardNo();
        credit = user.getCredit();
        email = user.getEmail();
        name = user.getName();
        tel = user.getTel();
        username = user.getUsername();
        if(user.getGrade()!=null){
            grade = user.getGrade().getGrade();
            maxHoldings = user.getGrade().getMaxHoldings();
            maxBorrowTime = user.getGrade().getMaxBorrowTime();
            maxReserveTime = user.getGrade().getMaxReserveTime();
        }
    }
}
