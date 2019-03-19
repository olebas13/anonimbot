package com.olebas.anonimbot.logger;

public enum LogLevel {

    STRANGE("STRANGE"),
    SUCCESS("SUCCESS");

    private final String mValue;

    LogLevel(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
