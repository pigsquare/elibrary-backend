package db2.elibrary.entity.enums;

public enum ReserveStatusEnum {
    WAITING,    // 已经提交预约，等待图书
    RESERVED,   // 图书已经预约成功，等待办理借阅
    CANCELLED,  // 用户主动取消
    CLOSED,     // 因为超时关闭预约
    FINISHED;    // 预约已完成
    public String getChineseName(){
        switch (this){
            case WAITING: return "等待锁定图书";
            case RESERVED: return "图书已预约成功，等待办理借阅";
            case CANCELLED: return "用户取消";
            case CLOSED: return "因超时关闭预约";
            case FINISHED: return "预约已完成";
            default: return this.name();
        }
    }
}
