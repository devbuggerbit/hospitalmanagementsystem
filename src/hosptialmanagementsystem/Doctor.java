package hosptialmanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {

    private Connection connection;

    public Doctor(Connection connection) {
        this.connection = connection;
    }

    public void viewDoctors() {
        String query = "SELECT * FROM doctors"; // Ensure the table name is correct
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Doctors: ");
            System.out.println("+------------+----------------------+--------------------+");
            System.out.println("| Doctor Id  | Name                 | Specialization     |");
            System.out.println("+------------+----------------------+--------------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("|%-12d|%-22s|%-20s|\n", id, name, specialization);
                System.out.println("+------------+----------------------+--------------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorsById(int id) {
        String query = "SELECT * FROM doctors WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Return true if a row exists, false otherwise
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
