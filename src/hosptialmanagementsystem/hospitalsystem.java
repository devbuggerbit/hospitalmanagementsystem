package hosptialmanagementsystem;

import java.sql.*;
import java.util.Scanner;

public class hospitalsystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "DEVANSH14@sam";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            patient patient = new patient(connection, scanner);
            Doctor doctor = new Doctor(connection);

            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add patient");
                System.out.println("2. View patient");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        patient.addpatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewpatient();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookappointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("Exiting the system. Goodbye!");
                        return;
                    default:
                        System.out.println("Enter a valid choice");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookappointment(patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        System.out.print("ENTER PATIENT ID: ");
        int patientId = scanner.nextInt();
        System.out.print("ENTER DOCTOR ID: ");
        int doctorId = scanner.nextInt();
        System.out.print("ENTER APPOINTMENT DATE (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        if (patient.getpatientId(patientId) && doctor.getDoctorsById(doctorId)) {
            if (checkDoctoravailability(doctorId, appointmentDate, connection)) {
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery)) {
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setDate(3, java.sql.Date.valueOf(appointmentDate));

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment booked successfully!");
                    } else {
                        System.out.println("Failed to book appointment.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error booking appointment: " + e.getMessage());
                }
            } else {
                System.out.println("Doctor is not available on this date.");
            }
        } else {
            System.out.println("Either the doctor or the patient does not exist!");
        }
    }

    public static boolean checkDoctoravailability(int doctorId, String appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setDate(2, java.sql.Date.valueOf(appointmentDate));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
