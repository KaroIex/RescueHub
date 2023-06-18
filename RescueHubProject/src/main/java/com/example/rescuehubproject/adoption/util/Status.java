package com.example.rescuehubproject.adoption.util;

import com.example.rescuehubproject.accounts.execeptions.RoleNotFoundException;

public enum Status {
    NEW,
    EXPECT,
    ACCEPT;

    public static Status getStatus(String status) {
        return switch (status) {
            case "NEW" -> NEW;
            case "EXPECT" -> EXPECT;
            case "ACCEPT" -> ACCEPT;
            default -> throw new RoleNotFoundException("status not found!");
        };
    }
}
