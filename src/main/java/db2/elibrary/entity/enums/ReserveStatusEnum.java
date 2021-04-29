package db2.elibrary.entity.enums;

public enum ReserveStatusEnum {
    WAITING,    // 已经提交预约，等待图书
    RESERVED,   // 图书已经预约成功，等待办理借阅
    CANCELLED,  // 用户主动取消
    CLOSED,     // 因为超时关闭预约
    FINISHED    // 预约已完成
}
