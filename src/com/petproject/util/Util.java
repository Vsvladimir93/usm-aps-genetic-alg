package com.petproject.util;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public final class Util {

    private static boolean isLogEnabled = true;
    private static final Random r = new Random();

    private Util() {
    }

    public static void enableLog(boolean enable) {
        isLogEnabled = enable;
    }

    public static int getNextInt(int min, int max, Scanner s) {
        try {
            int next = s.nextInt();
            if (next < min || next > max) {
                System.out.printf("Введите число в пределах от %d до %d\n", min, max);
                return getNextInt(min, max, s);
            } else {
                return next;
            }
        } catch (InputMismatchException e) {
            System.out.printf("Введите число в пределах от %d до %d\n", min, max);
            return getNextInt(min, max, s);
        }
    }

    public static boolean getYesOrNo(Scanner s) {
        String next = s.nextLine();
        if (next.equals("y") || next.equals("n")) {
            return next.equals("y");
        } else {
            System.out.println("Введите 'y' или 'n'.");
            return getYesOrNo(s);
        }
    }

    public static void print(String message, Object... args) {
        if (isLogEnabled)
            System.out.printf(message.concat("\n"), args);
    }

    public static Random getRandom() {
        return r;
    }

}
