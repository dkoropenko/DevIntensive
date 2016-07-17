package com.softdesign.devintensive.utils;

import okhttp3.HttpUrl;

/**
 * Created by smalew on 10.07.16.
 */
public interface AppConfig {
    HttpUrl BASE_URL = HttpUrl.parse("http://devintensive.softdesign-apps.ru/api/");
    long MAX_CONNECT_TIMEOUT = 3000;
    long MAX_READ_TIMEOUT = 5000;
    long START_DELAY = 3000;
}
