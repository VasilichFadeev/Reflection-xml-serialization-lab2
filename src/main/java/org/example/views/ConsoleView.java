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
            System.out.println("❌ Ошибка: введите число!");
            scanner.next();
        }

        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    public String readString(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (input == null) input = "";

            if (input.trim().isEmpty()) {
                System.out.println("❌ Ошибка: поле не может быть пустым!");
            }
        } while (input.trim().isEmpty());

        return input.trim();
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("❌ Ошибка: введите число!");
                continue;
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Ошибка: введите корректное число!");
            }
        }
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }
}
