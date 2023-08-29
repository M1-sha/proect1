import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// DTO клас
class DataTransferObject {
    private String name;
    private Date dateOfBirth;
    private String phoneNumber;
    private String email;

    public DataTransferObject(String name, Date dateOfBirth, String phoneNumber, String email) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // Геттери та сеттери

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

// Операції CRUD
class CRUDOperations {
    private List<DataTransferObject> data = new ArrayList<>();

    public void createData(DataTransferObject dto) {
        data.add(dto);
    }

    public List<DataTransferObject> readData() {
        return data;
    }

    public void updateData(int index, DataTransferObject dto) {
        if (index >= 0 && index < data.size()) {
            data.set(index, dto);
        }
    }

    public void deleteData(int index) {
        if (index >= 0 && index < data.size()) {
            data.remove(index);
        }
    }
}

// Головний клас додатку
public class Main {
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_QUIT = "quit";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_SHOW_ALL = "show all";
    private static final String COMMAND_PRINT_ALL = "print all";
    private static final String COMMAND_CREATE = "create";
    private static final String COMMAND_DELETE = "delete";

    private static final String JSON_FILE_PATH = "data.json";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    private static final Scanner scanner = new Scanner(System.in);
    private static final CRUDOperations crudOperations = new CRUDOperations();

    public static void main(String[] args) {
        while (true) {
            System.out.print("Введіть команду: ");
            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase(COMMAND_EXIT) || command.equalsIgnoreCase(COMMAND_QUIT)) {
                System.out.println("Додаток завершує роботу.");
                break;
            } else if (command.equalsIgnoreCase(COMMAND_HELP)) {
                printHelp();
            } else if (command.equalsIgnoreCase(COMMAND_SHOW_ALL)) {
                showAllData();
            } else if (command.equalsIgnoreCase(COMMAND_PRINT_ALL)) {
                printAllData();
            } else if (command.equalsIgnoreCase(COMMAND_CREATE)) {
                createData();
            } else if (command.equalsIgnoreCase(COMMAND_DELETE)) {
                deleteData();
            } else {
                System.out.println("Невідома команда. Введіть 'help', щоб переглянути список команд.");
            }
        }
    }

    private static void printHelp() {
        System.out.println("Список команд:");
        System.out.println("  - exit, quit: Завершити роботу додатку");
        System.out.println("  - help: Показати список команд");
        System.out.println("  - show all: Показати всі дані");
        System.out.println("  - print all: Надрукувати всі дані");
        System.out.println("  - create: Створити новий запис");
        System.out.println("  - delete: Видалити контакт");
    }

    private static void showAllData() {
        List<DataTransferObject> data = crudOperations.readData();
        if (data.isEmpty()) {
            System.out.println("Немає наявних даних.");
        } else {
            for (DataTransferObject dto : data) {
                System.out.println("Ім'я: " + dto.getName() + ", Дата народження: " + DATE_FORMAT.format(dto.getDateOfBirth()) +
                        ", Номер телефону: " + dto.getPhoneNumber() + ", Email: " + dto.getEmail());
            }
        }
    }

    private static void printAllData() {
        List<DataTransferObject> data = crudOperations.readData();
        try {
            FileWriter fileWriter = new FileWriter(JSON_FILE_PATH);
            fileWriter.write("[\n");
            for (int i = 0; i < data.size(); i++) {
                DataTransferObject dto = data.get(i);
                String json = "{\"name\":\"" + dto.getName() + "\",\"dateOfBirth\":\"" + DATE_FORMAT.format(dto.getDateOfBirth()) +
                        "\",\"phoneNumber\":\"" + dto.getPhoneNumber() + "\",\"email\":\"" + dto.getEmail() + "\"}";
                fileWriter.write(json);
                if (i < data.size() - 1) {
                    fileWriter.write(",\n");
                } else {
                    fileWriter.write("\n");
                }
            }
            fileWriter.write("]\n");
            fileWriter.close();
            System.out.println("Дані успішно надруковані у файл " + JSON_FILE_PATH);
        } catch (IOException e) {
            System.out.println("Помилка при записі даних у файл.");
            e.printStackTrace();
        }
    }

    private static void createData() {
        System.out.print("Введіть ім'я: ");
        String name = scanner.nextLine().trim();

        System.out.print("Введіть дату народження (у форматі дд-мм-рррр): ");
        String dateOfBirthStr = scanner.nextLine().trim();
        Date dateOfBirth = null;
        try {
            dateOfBirth = DATE_FORMAT.parse(dateOfBirthStr);
        } catch (ParseException e) {
            System.out.println("Некоректний формат дати народження. Використайте формат дд-мм-рррр.");
            return;
        }

        System.out.print("Введіть номер телефону: ");
        String phoneNumber = scanner.nextLine().trim();

        System.out.print("Введіть електронну пошту: ");
        String email = scanner.nextLine().trim();

        DataTransferObject dto = new DataTransferObject(name, dateOfBirth, phoneNumber, email);
        crudOperations.createData(dto);
        System.out.println("Дані успішно створено.");
    }

    private static void deleteData() {
        System.out.print("Введіть індекс контакту, який потрібно видалити: ");
        int index = scanner.nextInt();
        scanner.nextLine(); // Читання символу нового рядка після введення числа

        crudOperations.deleteData(index);
        System.out.println("Контакт успішно видалено.");
    }
}
