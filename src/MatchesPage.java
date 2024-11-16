import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MatchesPage extends JFrame {
    private int loggedInUserId;

    public MatchesPage(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;

        setTitle("Your Matches");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel matchesPanel = new JPanel();
        matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));

        loadMatches(matchesPanel);

        add(new JScrollPane(matchesPanel), BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadMatches(JPanel panel) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT p.name, p.age, p.origin_city, p.destination_city, p.phone, p.email " +
                           "FROM profiles p " +
                           "JOIN likes l1 ON l1.target_user_id = p.id " +
                           "JOIN likes l2 ON l2.user_id = p.id AND l2.target_user_id = l1.user_id " +
                           "WHERE l1.user_id = ? AND l1.liked = TRUE AND l2.liked = TRUE";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
    
            if (!rs.isBeforeFirst()) {  // Check if ResultSet is empty
                panel.add(new JLabel("No matches yet. Like profiles to find matches!"));
            } else {
                while (rs.next()) {
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String originCity = rs.getString("origin_city");
                    String destinationCity = rs.getString("destination_city");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");
    
                    JLabel matchLabel = new JLabel(
                        "<html>" + name + ", " + age + " (" + originCity + " -> " + destinationCity + ")<br/>" +
                        "Phone: " + phone + "<br/>" +
                        "Email: " + email + "</html>"
                    );
                    panel.add(matchLabel);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
}
