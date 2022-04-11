package com.bank.test;

import com.bank.utils.Province;
import com.bank.utils.SM3;
import com.bank.utils.WorkState;

import java.io.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(SM3.encrypt("123456"));
        System.out.println(SM3.encryptWithSalt("123456"));
    }
}
