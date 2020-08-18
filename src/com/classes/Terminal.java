package com.classes;


import java.util.Scanner;

public class Terminal {
    private static Scanner sc = new Scanner(System.in);

    public static String readLine(String message){
        System.out.println(message);
        return sc.nextLine();
    }

    public static boolean checkKey(String cha){
        String alphabet = " 123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZаАбБвВгГдДеЕёЁжЖзЗиИйЙкКлЛмМнНоОпПрРсСтТуУфФхХшШщЩъЪыЫьЬэЭюЮяЯ,.;{}()\"-=+*/\\@";
        return alphabet.contains(cha);
    }

    public static void sleep(long millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }
    }
}
