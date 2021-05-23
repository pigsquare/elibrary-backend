package db2.elibrary.entity.enums;

public enum  BookStatusEnum {
    AVAILABLE,      // 可借
    BORROWED,       // 已借出
    RESERVED,       // 未借出，并被预定保留
    UNAVAILABLE,    // 不可借出
    DAMAGED,        // 被玩坏了
    LOST,           // 玩丢了
    OTHER;
    public String getChineseName(){
        switch (this){
            case LOST: return "丢失";
            case DAMAGED: return "图书损坏";
            case BORROWED: return "已借出";
            case AVAILABLE: return "可外借";
            case UNAVAILABLE: return "不可外借";
            case RESERVED: return "已被预约";
            default: return this.name();
        }
    }
}
