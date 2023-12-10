import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationFormWithDatabase {
    private JFrame frame;
    private JPanel panel;
    private JLabel nameLabel, deptLabel;
    private JTextField nameField, deptField;
    private JButton submitButton;

    public RegistrationFormWithDatabase() {
        frame = new JFrame("Registration Form");
        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        nameLabel = new JLabel("Name:");
        deptLabel = new JLabel("Department:");

        nameField = new JTextField();
        deptField = new JTextField();

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitRegistration();
            }
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(deptLabel);
        panel.add(deptField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(submitButton);

        frame.add(panel);
        frame.setSize(300, 150);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void submitRegistration() {
        String name = nameField.getText();
        String dept = deptField.getText();

        // Database connection details
        String url = "jdbc:mysql://localhost:3306/studentDB";
        String username = "root";
        String password = "palak1234";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO student (name, dept) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, dept);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(frame, "Registration submitted successfully.");
                // Clear the fields after submission
                nameField.setText("");
                deptField.setText("");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: Registration submission failed.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RegistrationFormWithDatabase();
            }
        });
    }
}