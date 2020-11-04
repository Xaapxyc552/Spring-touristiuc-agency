package com.skidchenko.dz2;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(
                new View(),
                new Model());

        controller.execute();
    }
}
