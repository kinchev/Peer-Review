package com.telerik.peer.utils;

public class QueryBuilderUtils {
    public static String like(String parameter) {
        return String.format("%%%s%%", parameter);
    }
}
