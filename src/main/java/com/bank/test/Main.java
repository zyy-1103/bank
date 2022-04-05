package com.bank.test;

import com.bank.utils.SM3;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {
        System.out.println(SM3.encrypt("222333zm"));
    }
}
