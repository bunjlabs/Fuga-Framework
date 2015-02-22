/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.showvars.sweetie.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author show
 */
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
    }

    public static String getMimeTypeByExt(String ext) {
        return mimeTypes.get(ext.toLowerCase());
    }
}
