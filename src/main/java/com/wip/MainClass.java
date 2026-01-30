package com.wip;

public class MainClass {

    public int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        MainClass mc = new MainClass();

        while (true) {
            System.out.println(mc.add(10, 20));
            System.out.println("Java for Docker! Code is pulled from GitHub Repository");
            System.out.println("Updated code after adding Jenkins file to repository");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
