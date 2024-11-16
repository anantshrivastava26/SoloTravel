import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CancelHotelBookingPage extends JFrame {
    private int currentUserId;
    private JComboBox<Integer> bookingDropdown;

    public CancelHotelBookingPage(int currentUserId) {
        this.currentUserId = currentUserId;

        setTitle("Cancel Hotel Booking");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        bookingDropdown = new JComboBox<>();
        loadBookings();

        JButton cancelButton = new JButton("Cancel Booking");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Integer selectedBookingId = (Integer) bookingDropdown.getSelectedItem();
                if (selectedBookingId != null) {
                    cancelBooking(selectedBookingId);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Booking ID to Cancel:"));
        panel.add(bookingDropdown);

        add(panel, BorderLayout.CENTER);
        add(cancelButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadBookings() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT booking_id FROM hotel_bookings WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, currentUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookingDropdown.addItem(rs.getInt("booking_id"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void cancelBooking(int bookingId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "DELETE FROM hotel_bookings WHERE booking_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bookingId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Booking canceled successfully.");
                bookingDropdown.removeItem(bookingId); // Remove the canceled booking from the dropdown
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel booking.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        dispose();
    }
}
