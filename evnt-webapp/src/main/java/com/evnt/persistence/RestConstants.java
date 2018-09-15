package com.evnt.persistence;

public class RestConstants {
    private static final String HOST = "http://127.0.0.1:";
    private static final String EVNT_API_PORT = "8081";
    private static final String EVNT_API = "/evnt/api/";
    private static final String MAIL_API_PORT = "8082";
    private static final String MAIL_API = "/mail/api/";

    public static final String EVNT_API_BASE = HOST+EVNT_API_PORT+EVNT_API;
    public static final String MAIL_API_BASE = HOST+MAIL_API_PORT+MAIL_API;
}
