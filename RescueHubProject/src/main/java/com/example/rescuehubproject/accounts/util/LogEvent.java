package com.example.rescuehubproject.accounts.util;

public enum LogEvent {
    CREATE_USER("CREATE_USER"),
    CHANGE_PASSWORD("CHANGE_PASSWORD"),
    ACCESS_DENIED("ACCESS_DENIED"),
    LOGIN_FAILED("LOGIN_FAILED"),
    GRANT_ROLE("GRANT_ROLE"),
    REMOVE_ROLE("REMOVE_ROLE"),
    LOCK_USER("LOCK_USER"), //todo
    UNLOCK_USER("UNLOCK_USER"), //todo
    DELETE_USER("DELETE_USER"),
    BRUTE_FORCE("BRUTE_FORCE"), //todo
    AUTH_SUCCESS("AUTH_SUCCESS"), //niepotrzebne ale moze byc
    DEFAULT_ACTION("DEFAULT_ACTION");

    private final String eventName;

    LogEvent(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
