import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShowHotelBookingsPage extends JFrame {
    private int currentUserId;

    public ShowHotelBookingsPage(int currentUserId) {
        this.currentUserId = currentUserId;

        setTitle("Your Hotel Bookings");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel bookingsPanel = new JPanel();
        bookingsPanel.setLayout(new BoxLayout(bookingsPanel, BoxLayout.Y_AXIS));

        loadHotelBookings(bookingsPanel);

        JButton cancelBookingButton = new JButton("Cancel Booking");
        cancelBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CancelHotelBookingPage(currentUserId).setVisible(true);
                dispose();
            }
        });

        add(new JScrollPane(bookingsPanel), BorderLayout.CENTER);
        add(cancelBookingButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadHotelBookings(JPanel panel) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT booking_id, destination, hotel_name, booking_date FROM hotel_bookings WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, currentUserId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                panel.add(new JLabel("No hotel bookings found."));
            } else {
                while (rs.next()) {
                    int bookingId = rs.getInt("booking_id");
                    String destination = rs.getString("destination");
                    String hotelName = rs.getString("hotel_name");
                    String bookingDate = rs.getString("booking_date");

                    JLabel bookingLabel = new JLabel(
                        "<html>Booking ID: " + bookingId +
                        "<br>Destination: " + destination +
                        "<br>Hotel: " + hotelName +
                        "<br>Booked On: " + bookingDate + "<br><br></html>"
                    );
                    panel.add(bookingLabel);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
