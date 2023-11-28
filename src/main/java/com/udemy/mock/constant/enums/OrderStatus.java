package com.udemy.mock.constant.enums;

import java.util.Objects;

public enum OrderStatus {

    CREATED(0, "đã tạo đơn"),
    PAID(1, "đã thanh toán"),
    COMPLETED(2, "hoàn thành");


    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static boolean isOrderStatus(Integer code) {
        if (Objects.nonNull(code)) {
            for (OrderStatus e : values()) {
                if (e.code.equals(code)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isCompleteStatus(Integer code) {
        return Objects.equals(COMPLETED.getCode(), code);
    }
    public static boolean isCreatedStatus(Integer code) {
        return Objects.equals(CREATED.getCode(), code);
    }
    public static boolean isPaidStatus(Integer code) {
        return Objects.equals(PAID.getCode(), code);
    }

    public static boolean isCreateStatus(Integer code) {
        return Objects.equals(CREATED.getCode(), code);
    }

    OrderStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;
}
