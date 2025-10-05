package org.example.views;

import java.util.Scanner;

public class ConsoleView {
    private final Scanner scanner = new Scanner(System.in);

    public int showMenu() {
        System.out.println("\nМеню:");
        System.out.println("1 — Ввести данные и сохранить в XML");
        System.out.println("2 — Загрузить из XML");
        System.out.println("3 — Выход");
        System.out.print("Ваш выбор: ");

        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка: введите число!");
            scanner.next();
        }

        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка: нужно ввести число!");
            System.out.print(prompt);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }
}
