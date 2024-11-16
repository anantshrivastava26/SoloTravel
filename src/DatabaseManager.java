import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/solo_travel_app";
    private static final String USER = "root";
    private static final String PASSWORD = "Aster@2405";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ResultSet getProfiles(String destination) throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT * FROM profiles WHERE destination_city = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, destination);
        return stmt.executeQuery();
    }

    public static void saveLike(int userId, int likedUserId) throws SQLException {
        Connection conn = getConnection();
        String query = "INSERT INTO likes (user_id, liked_user_id) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        stmt.setInt(2, likedUserId);
        stmt.executeUpdate();
    }

    public static ResultSet checkMatch(int userId, int likedUserId) throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT * FROM likes WHERE user_id = ? AND liked_user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, likedUserId);
        stmt.setInt(2, userId);
        return stmt.executeQuery();
    }

    public static ResultSet getMatches(int userId) throws SQLException {
        Connection conn = getConnection();
        String query = "SELECT p.name, p.email, p.phone FROM profiles p JOIN likes l ON p.id = l.liked_user_id " +
                       "WHERE l.user_id = ? AND EXISTS (SELECT * FROM likes WHERE user_id = p.id AND liked_user_id = ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, userId);
        stmt.setInt(2, userId);
        return stmt.executeQuery();
    }
    
}
