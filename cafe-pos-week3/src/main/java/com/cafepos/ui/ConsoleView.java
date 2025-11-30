package com.cafepos.ui;

public final class ConsoleView {
    public void print(String s) {
        System.out.println(s);
    }

    public void dash() {
        System.out.println("------------------------------");
    }

    public void sign() {
        System.out.println("==============================");
    }

    public void intro() {
        this.sign();
        this.print(" Final Demo | CS4928" +
                "\n         Group 35" +
                "\n Leo O'Shea      22342761" +
                "\n Aron Calvert    22370274"
        );
        this.sign();

        this.print("\n      Welcome to Cafe POS!\n");
        this.sign();
        this.print("           ~ Menu ~        ");
        this.sign();

        this.print("Coffee:" +
                "\n  ESP - Espresso" +
                "\n  LAT - Latte" +
                "\n  CAP - Cappuccino");

        this.dash();

        this.print("Add ons:" +
                "\n  SHOT - Extra Shot" +
                "\n  OAT  - Oat Milk" +
                "\n  SYP  - Syrup" +
                "\n  L    - Large Size");

        this.sign();
    }
}
