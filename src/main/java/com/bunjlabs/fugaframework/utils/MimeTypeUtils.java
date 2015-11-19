package com.bunjlabs.fugaframework.utils;

import java.util.HashMap;
import java.util.Map;

public class MimeTypeUtils {

    private final static Map<String, String> mimeTypes = new HashMap<>();

    static {
        mimeTypes.put("css", "text/css");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("jpeg", "jpg,image/jpeg");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("ico", "image/x-icon");
        mimeTypes.put("bmp", "image/x-ms-bmp");
        mimeTypes.put("js", "application/x-javascript");
        mimeTypes.put("svg", "image/svg+xml");
        mimeTypes.put("mp4", "video/mp4");
    }

    public static String getMimeTypeByExt(String ext) {
        return mimeTypes.get(ext.toLowerCase());
    }
}
