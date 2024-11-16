import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TinderLikePage extends JFrame {
    private int loggedInUserId; 
    private List<Profile> profiles;
    private int currentIndex;
    private JLabel profileInfoLabel;

    public TinderLikePage(String location, int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
        this.profiles = new ArrayList<>();
        this.currentIndex = 0;

        setTitle("Tinder-like Solo Travelers");
        setSize(450, 300);
        setLocationRelativeTo(null); // Center the window on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Profile display area
        profileInfoLabel = new JLabel("Loading profiles...", SwingConstants.CENTER);
        profileInfoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        profileInfoLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        profileInfoLabel.setForeground(new Color(54, 81, 207));

        // Buttons
        JButton likeButton = new JButton("❤️ Like");
        JButton dislikeButton = new JButton("👎 Dislike");
        styleButton(likeButton);
        styleButton(dislikeButton);

        // Panels
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BorderLayout());
        profilePanel.add(profileInfoLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(likeButton);
        buttonPanel.add(dislikeButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        add(profilePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        likeButton.addActionListener(e -> handleLikeDislike(true));
        dislikeButton.addActionListener(e -> handleLikeDislike(false));

        loadProfiles(location);
        displayCurrentProfile();
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(85, 107, 47));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void loadProfiles(String location) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT id, name, age, origin_city, destination_city FROM profiles WHERE destination_city = ? AND id != ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, location);
            stmt.setInt(2, loggedInUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String originCity = rs.getString("origin_city");
                String destinationCity = rs.getString("destination_city");
                profiles.add(new Profile(id, name, age, originCity, destinationCity));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void displayCurrentProfile() {
        if (currentIndex < profiles.size()) {
            Profile currentProfile = profiles.get(currentIndex);
            profileInfoLabel.setText(
                    "<html><div style='text-align: center;'>" +
                    currentProfile.name + ", " + currentProfile.age +
                    "<br>(" + currentProfile.originCity + " ➔ " + currentProfile.destinationCity + ")" +
                    "</div></html>");
        } else {
            profileInfoLabel.setText("No more profiles available.");
        }
    }

    private void handleLikeDislike(boolean liked) {
        if (currentIndex < profiles.size()) {
            Profile currentProfile = profiles.get(currentIndex);
            saveLikeOrDislike(currentProfile.id, liked);
            currentIndex++;
            displayCurrentProfile();
        }
    }

    private void saveLikeOrDislike(int targetUserId, boolean liked) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "INSERT INTO likes (user_id, target_user_id, liked) VALUES (?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE liked = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, loggedInUserId);
            stmt.setInt(2, targetUserId);
            stmt.setBoolean(3, liked);
            stmt.setBoolean(4, liked);
            stmt.executeUpdate();

            if (liked && checkMutualLike(targetUserId)) {
                JOptionPane.showMessageDialog(this, "It's a match! 🎉");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkMutualLike(int targetUserId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM likes WHERE user_id = ? AND target_user_id = ? AND liked = TRUE";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, targetUserId);
            stmt.setInt(2, loggedInUserId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private class Profile {
        int id;
        String name;
        int age;
        String originCity;
        String destinationCity;

        Profile(int id, String name, int age, String originCity, String destinationCity) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.originCity = originCity;
            this.destinationCity = destinationCity;
        }
    }
}
