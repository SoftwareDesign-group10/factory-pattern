import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        RegisteredUserFactory factory =
                new RegisteredUserFactory();

        try {

            System.out.print("Enter your email: ");
            String email = input.nextLine();

            System.out.print("Enter your password: ");
            String password = input.nextLine();

            System.out.print("Enter your username: ");
            String userName = input.nextLine();

            System.out.print(
                    "Enter account type (STUDENT, STAFF, FACULTY): ");
            String accountType = input.nextLine();

            RegisteredUser user =
                    factory.createUser(
                            email,
                            password,
                            accountType,
                            userName);

            System.out.println("\nAccount created successfully!");

            System.out.println(
                    "Email: " + user.getEmail());

            System.out.println(
                    "Username: " + user.getUserName());

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "\nAccount creation failed.");

            System.out.println(
                    "Reason: " + e.getMessage());
        }

        input.close();
    }
}