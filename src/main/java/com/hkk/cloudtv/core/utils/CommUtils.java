package com.hkk.cloudtv.core.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CommUtils {

    public static final String randomString(int length) {
        char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                .toCharArray();
        if (length < 1) {
            return "";
        }
        Random randGen = new Random();
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];// nextInt:这里是一个方法的重载，参数的内容是指定范围
        }
        return new String(randBuffer);
    }
}
