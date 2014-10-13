package com.sweetieframework.foundation;

public class RequestMethodUtil {

    public static RequestMethod valueOf(String name) {
        switch (name) {
            case "GET":
                return RequestMethod.GET;
            case "POST":
                return RequestMethod.POST;
            case "PUT":
                return RequestMethod.PUT;
            case "DELETE":
                return RequestMethod.DELETE;
            case "HEAD":
                return RequestMethod.HEAD;
            default:
                return RequestMethod.GET;
        }
    }
}
