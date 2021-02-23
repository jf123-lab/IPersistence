package com.config;

import java.io.InputStream;

/**
 * 将xml文件转成字节输入流存在内存中
 */
public class Resources {
    public static InputStream getResourceAsStream(String path) {
        InputStream resourceAsStream = Resources.class.getClassLoader().getResourceAsStream(path);

        return resourceAsStream;
    }
}
