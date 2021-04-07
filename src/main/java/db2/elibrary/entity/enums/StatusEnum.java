package db2.elibrary.entity.enums;

public enum  StatusEnum {
    AVAILABLE,      // 可借
    BORROWED,       // 已借出
    BOOKED,         // 已借出，并被预定
    RESERVED,       // 未借出，并被预定保留
    UNAVAILABLE,    // 不可借出
    DAMAGED,        // 被玩坏了
    LOST,           // 玩丢了
    OTHER
}
