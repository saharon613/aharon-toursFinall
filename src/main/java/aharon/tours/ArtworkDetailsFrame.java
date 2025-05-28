package aharon.tours;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ArtworkDetailsFrame extends JFrame {
    public ArtworkDetailsFrame(Artwork artwork) {
        setTitle(artwork.title);
        setSize(600, 700);
        setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());

        String imageUrl = "https://www.artic.edu/iiif/2/" + artwork.imageId + "/full/843,/0/default.jpg";

        try {
            Image originalImage = ImageIO.read(new URL(imageUrl));
            Image scaledImage = originalImage.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(icon);
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (IOException e) {
            imagePanel.add(new JLabel("No image"), BorderLayout.CENTER);
        }

        add(imagePanel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        if (artwork.title != null && !artwork.title.isEmpty()) {
            addField(detailsPanel, "Title: " + artwork.title);
        }

        if (artwork.description != null && !artwork.description.isEmpty()) {
            JTextArea area = new JTextArea("Description: " + artwork.description.replaceAll("<[^>]*>",
                    ""));
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setEditable(false);
            area.setOpaque(false);
            area.setBorder(null);
            area.setAlignmentX(Component.LEFT_ALIGNMENT);
            detailsPanel.add(area);
        } else {
            detailsPanel.add(new JLabel("No description"));
        }

        JScrollPane scrollPane = new JScrollPane(detailsPanel);
        add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addField(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        panel.add(label);
    }
}


