import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentDatabaseOperations {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/studentD_B";
        String username = "root";
        String password = "palak1234";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            // Step 1: Create student table
            createStudentTable(connection);

            // Step 2: Insert 5 records
            insertRecords(connection);

            // Step 3: Update dept to "CSE" for reg_no 101
            updateRecord(connection, 101, "CSE");

            // Step 4: Delete the record with reg_no 103
            deleteRecord(connection, 103);

            // Step 5: Display all records
            displayAllRecords(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createStudentTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS student ("
                + "reg_no INT PRIMARY KEY,"
                + "name VARCHAR(255),"
                + "dept VARCHAR(255))";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    private static void insertRecords(Connection connection) throws SQLException {
        String insertSQL = "INSERT INTO student (reg_no, name, dept) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            // Insert 5 records
            for (int i = 1; i <= 5; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.setString(2, "Student" + i);
                preparedStatement.setString(3, "Dept" + i);
                preparedStatement.executeUpdate();
            }
        }
    }

    private static void updateRecord(Connection connection, int regNo, String newDept) throws SQLException {
        String updateSQL = "UPDATE student SET dept = ? WHERE reg_no = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, newDept);
            preparedStatement.setInt(2, regNo);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Record with reg_no " + regNo + " updated successfully.");
            } else {
                System.out.println("Record with reg_no " + regNo + " not found.");
            }
        }
    }

    private static void deleteRecord(Connection connection, int regNo) throws SQLException {
        String deleteSQL = "DELETE FROM student WHERE reg_no = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, regNo);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Record with reg_no " + regNo + " deleted successfully.");
            } else {
                System.out.println("Record with reg_no " + regNo + " not found.");
            }
        }
    }

    private static void displayAllRecords(Connection connection) throws SQLException {
        String selectSQL = "SELECT * FROM student";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            System.out.println("Student Records:");
            System.out.println("Reg_No\tName\tDept");
            while (resultSet.next()) {
                int regNo = resultSet.getInt("reg_no");
                String name = resultSet.getString("name");
                String dept = resultSet.getString("dept");
                System.out.println(regNo + "\t" + name + "\t" + dept);
            }
        }
    }
}