import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class anothertest {


    static String global_username = "";

    static String global_password = "";

    public static void getInput() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter your username: ");
        global_username = input.nextLine();

        System.out.print("Enter your password: ");
        global_password = input.nextLine();

    }

    public static void saveCredentials(String username, String password) {
        String fileName = "login_credentials.txt";

        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(username + ":" + password + "\n");

            System.out.println("Credentials saved to " + fileName);
            System.out.println( username + password);

        } catch (IOException e) {
            System.out.println("An error occurred while saving credentials.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getInput();

        saveCredentials(global_username,global_password);

    }


}
