/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sweetieframework.foundation;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionTools {

    public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(packageName.replace('.', '/'));
        ArrayList<Class> classes = new ArrayList<>();
        while (resources.hasMoreElements()) {

            File dir = new File(resources.nextElement().getFile());
            //if(dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                System.out.println(file.getAbsolutePath());
            }
            //}
        }
        return classes.toArray(new Class[classes.size()]);
    }

}
