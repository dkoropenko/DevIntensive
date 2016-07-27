package com.softdesign.devintensive.data.network.req;

/**
 * Класс для отправки данных для авторизации
 * на сервер
 */
public class UserLoginReq {

    private String email;
    private String password;

    public UserLoginReq(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
