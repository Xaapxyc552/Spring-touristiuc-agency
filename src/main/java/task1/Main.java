package com.skidchenko.dz1;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Controller controller = new Controller(scanner,
                new Model(),
                new View());


        controller.executeProgram();
    }
}


