import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class QRimage extends JFrame {
    private Map<String, String> qrImagePaths;

    public QRimage(String destination) {
        setTitle("QR Code for " + destination);
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        qrImagePaths = new HashMap<>();
        qrImagePaths.put("Delhi", "lib\\DelhiQR.jpg");
        qrImagePaths.put("Mumbai", "lib\\MumbaiQR.jpg");
        qrImagePaths.put("Bangalore", "lib\\BangaloreQR.jpg");
        qrImagePaths.put("Chennai", "lib\\ChennaiQR.jpg");

        JLabel qrLabel = new JLabel();
        ImageIcon qrIcon = loadQRImage(destination);
        qrLabel.setIcon(qrIcon);
        add(qrLabel);
    }

    private ImageIcon loadQRImage(String destination) {
        String imagePath = qrImagePaths.getOrDefault(destination, "path/to/defaultQR.jpg");
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(400, 700, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}