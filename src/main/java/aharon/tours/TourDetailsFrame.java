package aharon.tours;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class TourDetailsFrame extends JFrame {
    private final TourDetailsController controller;

    public TourDetailsFrame(Tour tour, TourDetailsController controller) {
        this.controller = controller;

        setTitle(tour.title);
        setSize(800, 900);
        setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());

        try {
            Image image = ImageIO.read(new URL(tour.image));
            Image scaledImage = image.getScaledInstance(500, 400, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(icon);
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (IOException e) {
            imagePanel.add(new JLabel("No image"), BorderLayout.CENTER);
        }

        add(imagePanel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(detailsPanel);
        add(scrollPane, BorderLayout.CENTER);

        if (tour.artworkTitles != null && !tour.artworkTitles.isEmpty()) {
            for (String artworkTitle : tour.artworkTitles) {
                controller.getImage(artworkTitle, detailsPanel);
            }
        }
        else {
            addField(detailsPanel, "No Artwork Titles Provided");
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addField(JPanel panel, String text) {
        panel.add(new JLabel(text));
    }
}