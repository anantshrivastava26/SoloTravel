import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BookHotelPage extends JFrame {
    private int currentUserId;
    private JComboBox<String> hotelDropdown;
    private String selectedDestination;

    public BookHotelPage(int currentUserId, String selectedDestination) {
        this.currentUserId = currentUserId;
        this.selectedDestination = selectedDestination;

        setTitle("Book a Hotel");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        hotelDropdown = new JComboBox<>();

        loadHotels();

        JButton bookHotelButton = new JButton("Book Hotel");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Hotel:"));
        topPanel.add(hotelDropdown);

        add(topPanel, BorderLayout.CENTER);
        add(bookHotelButton, BorderLayout.SOUTH);

        bookHotelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedHotel = (String) hotelDropdown.getSelectedItem();
                if (selectedHotel != null) {
                    saveHotelBooking(selectedHotel);
                }
            }
        });

        setVisible(true);
    }

    private void loadHotels() {
        // Generate hotels dynamically based on destination
        String[] hotels = {
            selectedDestination + " Grand Hotel",
            selectedDestination + " City Inn",
            selectedDestination + " Luxury Suites",
            selectedDestination + " Budget Stay",
            selectedDestination + " Premium Inn"
        };

        for (String hotel : hotels) {
            hotelDropdown.addItem(hotel);
        }
    }

    private void saveHotelBooking(String hotel) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "INSERT INTO hotel_bookings (user_id, destination, hotel_name, booking_date) VALUES (?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, currentUserId);
            stmt.setString(2, selectedDestination);
            stmt.setString(3, hotel);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Hotel booked successfully!");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to book hotel.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
