import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SoloTravelApp extends JFrame {
    private int loggedInUserId; // Variable to store the logged-in user's ID

    public SoloTravelApp() {
        setTitle("Solo Travel App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));

        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        // Add components with layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);
        gbc.gridx = 1;
        panel.add(signUpButton, gbc);

        add(panel);

        // Login button action listener
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticateUser(username, password)) {
                    loggedInUserId = getUserId(username, password); // Get the logged-in user's ID
                    if (loggedInUserId != -1) {
                        MainMenuPage mainMenu = new MainMenuPage(loggedInUserId); // Pass user ID to main menu
                        mainMenu.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(SoloTravelApp.this, "Error retrieving user ID.");
                    }
                } else {
                    JOptionPane.showMessageDialog(SoloTravelApp.this, "Invalid username or password. Please try again.");
                }
            }
        });

        // Sign-up button action listener
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SignUpPage().setVisible(true);
            }
        });

        setVisible(true);
    }

    // Function to authenticate user
    private boolean authenticateUser(String username, String password) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM profiles WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if a matching user is found
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Function to get user ID based on username and password
    private int getUserId(String username, String password) {
        int userId = -1; // Default value if no user is found
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT id FROM profiles WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id"); // Fetch the user's ID if credentials match
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userId;
    }

    public static void main(String[] args) {
        new SoloTravelApp();
    }
}
