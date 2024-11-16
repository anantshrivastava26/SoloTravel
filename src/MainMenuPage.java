import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPage extends JFrame {

    public MainMenuPage(int currentUserId) {
        setTitle("Main Menu");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set layout and padding
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title label
        JLabel titleLabel = new JLabel("Main Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(20)); // Add spacing

        // Destination Dropdown
        JLabel destinationLabel = new JLabel("Select Destination:");
        destinationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        destinationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] locations = {"Delhi", "Mumbai", "Bangalore", "Chennai", "Hyderabad", "Kolkata", "Pune", "Jaipur", "Ahmedabad"};
        JComboBox<String> locationDropdown = new JComboBox<>(locations);
        locationDropdown.setMaximumSize(new Dimension(200, 25));
        locationDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(destinationLabel);
        panel.add(Box.createVerticalStrut(10)); // Spacing
        panel.add(locationDropdown);

        // Button panel with grid layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Add buttons with styling
        JButton soloTravellersButton = createButton("Solo Travellers");
        JButton travelGroupsButton = createButton("Travel Groups");
        JButton matchesButton = createButton("Matches");
        JButton bookFlightsButton = createButton("Book Flights");
        JButton showBookingsButton = createButton("Show Flight Bookings");
        JButton bookHotelsButton = createButton("Book Hotels");
        JButton showHotelsButton = createButton("Show Hotel Bookings");
        JButton logoutButton = createButton("Logout");

        // Add buttons to grid panel
        buttonPanel.add(soloTravellersButton);
        buttonPanel.add(travelGroupsButton);
        buttonPanel.add(matchesButton);
        buttonPanel.add(bookFlightsButton);
        buttonPanel.add(showBookingsButton);
        buttonPanel.add(bookHotelsButton);
        buttonPanel.add(showHotelsButton);
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel);

        add(panel);

        // Button action listeners
        soloTravellersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedLocation = (String) locationDropdown.getSelectedItem();
                new TinderLikePage(selectedLocation, currentUserId).setVisible(true);
            }
        });

        travelGroupsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedLocation = (String) locationDropdown.getSelectedItem();
                new QRimage(selectedLocation).setVisible(true); // Open QR Image based on location
            }
        });

        matchesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MatchesPage(currentUserId).setVisible(true);
            }
        });

        bookFlightsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedLocation = (String) locationDropdown.getSelectedItem();
                new BookFlightsPage(currentUserId, selectedLocation).setVisible(true);
            }
        });

        showBookingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ShowBookingsPage(currentUserId).setVisible(true);
            }
        });

        bookHotelsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedLocation = (String) locationDropdown.getSelectedItem();
                new BookHotelPage(currentUserId, selectedLocation).setVisible(true);
            }
        });

        showHotelsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ShowHotelBookingsPage(currentUserId).setVisible(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SoloTravelApp().setVisible(true);
            }
        });

        setLocationRelativeTo(null); // Center the frame
    }

    // Helper method to create styled buttons
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(120, 40));
        button.setFocusPainted(false);
        return button;
    }
}
