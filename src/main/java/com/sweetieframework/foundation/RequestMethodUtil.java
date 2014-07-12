package com.sweetieframework.foundation;

public class RequestMethodUtil {

    public static RequestMethod valueOf(String name) {
        switch (name) {
            case "POST":
                return RequestMethod.POST;
            default:
                return RequestMethod.GET;
        }
    }
}
