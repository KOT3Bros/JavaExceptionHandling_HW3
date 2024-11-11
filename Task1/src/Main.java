/*
Задание 1. Проверка корректности даты

Напишите приложение, которое будет запрашивать у пользователя следующие
данные в произвольном порядке, разделенные пробелом:
Фамилия Имя Отчество датарождения номертелефона пол
Форматы данных:
фамилия, имя, отчество - строки
дата_рождения - строка формата dd.mm.yyyy
номер_телефона - целое беззнаковое число без форматирования
пол - символ латиницей f или m.
Приложение должно проверить введенные данные по количеству. Если
количество не совпадает с требуемым, вернуть код ошибки, обработать его и
показать пользователю сообщение, что он ввел меньше и больше данных, чем
требуется.
Приложение должно попытаться распарсить полученные значения и выделить из
них требуемые параметры. Если форматы данных не совпадают, нужно бросить
исключение, соответствующее типу проблемы. Можно использовать встроенные
типы java и создать свои. Исключение должно быть корректно обработано,
пользователю выведено сообщение с информацией, что именно неверно.
Если всё введено и обработано верно, должен создаться файл с названием,
равным фамилии, в него в одну строку должны записаться полученные данные,
вида
<Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
Не забудьте закрыть соединение с файлом.
При возникновении проблемы с чтением-записью в файл, исключение должно
быть корректно обработано, пользователь должен увидеть стектрейс ошибки.
*/

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ваши данные одной строкой через пробел (Фамилия Имя Отчество Дата_рождения Номер_телефона Пол):");
        String input = scanner.nextLine();
        scanner.close();
        try {
            String[] data = input.split(" ");
            if (data.length != 6) {
                throw new IllegalArgumentException("Некорректное кол-во данных. Ожидалось, что их будет 6 (1.Фамилия 2.Имя 3.Отчество 4.Дата_рождения 5.Номер_телефона 6.Пол).");
            }
            String surname = data[0];
            String name = data[1];
            String middleName = data[2];
            LocalDate dateOfBirth = parseDate(data[3]);
            long phoneNumber = parsePhoneNumber(data[4]);
            char gender = parseGender(data[5]);
            writeToFile(surname, name, middleName, dateOfBirth, phoneNumber, gender);
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.err.println("Некорректно введена дата. Введите дату в формате DD.MM.YYYY.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LocalDate parseDate(String dateStr) throws
            DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    private static long parsePhoneNumber(String phoneNumberStr) {
        try {
            return Long.parseLong(phoneNumberStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный введён номера телефона.");
        }
    }

    private static char parseGender(String genderStr) {
        if (genderStr.length() != 1 ||
                !(genderStr.equalsIgnoreCase("м") || genderStr.equalsIgnoreCase("ж"))) {
            throw new IllegalArgumentException("Некорректно введён пол. Введите 'м' или 'ж'.");
        }
        return genderStr.toLowerCase().charAt(0);
    }

    private static void writeToFile(String surname, String name, String middleName, LocalDate dateOfBirth, long phoneNumber, char gender)
            throws IOException {
        String filename = surname + ".txt";
        String line = String.format("%s %s %s %s %d %c", surname, name, middleName, dateOfBirth, phoneNumber, gender);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(line);
            writer.newLine();
        }
    }
}