package com.softdesign.devintensive.utils;

/**
 * Класс для передачи сообщений из отдельного потока в главный.
 */
public class LoginInMessage {
    public final int sMessage;

    public LoginInMessage(int sMessage) {
        this.sMessage = sMessage;
    }
}
