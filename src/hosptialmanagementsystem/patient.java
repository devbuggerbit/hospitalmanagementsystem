package hosptialmanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class patient {

    private Connection connection;
    private Scanner scanner;

    public patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addpatient() {
        System.out.print("Enter Patient Name: ");
        String name = scanner.next();

        System.out.print("Enter Patient Age: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid age.");
            scanner.next(); // clear invalid input
        }
        int age = scanner.nextInt();

        System.out.print("Enter Patient Gender: ");
        String gender = scanner.next();

        String query = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient added successfully!");
            } else {
                System.out.println("Failed to add patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewpatient() {
        String query = "SELECT * FROM patients";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Patients:");
            System.out.println("+------------+----------------------+--------+-----------+");
            System.out.println("| Patient Id | Name                 | Age    | Gender    |");
            System.out.println("+------------+----------------------+--------+-----------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12d|%-22s|%-8d|%-11s|\n", id, name, age, gender);
                System.out.println("+------------+----------------------+--------+-----------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getpatientId(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
