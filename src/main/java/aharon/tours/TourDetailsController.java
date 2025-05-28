package aharon.tours;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class TourDetailsController {

    private final ToursService service;

    public TourDetailsController(ToursService service) {
        this.service = service;
    }

    public void getImage(String title, JPanel panel) {
        service.searchArtworks(title)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(SwingUtilities::invokeLater))
                .subscribe(searchResponse -> {
                    if (!searchResponse.data.isEmpty()) {
                        int artworkId = searchResponse.data.get(0).id;

                        service.getArtworkDetails(artworkId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.from(SwingUtilities::invokeLater))
                                .subscribe(detailsResponse -> {
                                    String imageId = detailsResponse.data.image_id;
                                    String artworkTitle = detailsResponse.data.title;
                                    Artwork artwork = detailsResponse.data;

                                    if (imageId != null) {
                                        String imageUrl = "https://www.artic.edu/iiif/2/" + imageId +
                                                "/full/843,/0/default.jpg";
                                        loadImage(imageUrl, artwork, panel);
                                    }
                                }, Throwable::printStackTrace);
                    }
                }, Throwable::printStackTrace);
    }

    private void loadImage(String imageUrl, Artwork artwork, JPanel panel) {
        Single.fromCallable(() -> ImageIO.read(new URL(imageUrl)))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(SwingUtilities::invokeLater))
                .subscribe(image -> {
                    Image scaled = image.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(scaled));
                    JLabel titleLabel = new JLabel(artwork.title, SwingConstants.CENTER);

                    JPanel imgPanel = new JPanel();
                    imgPanel.setLayout(new BoxLayout(imgPanel, BoxLayout.Y_AXIS));
                    imgPanel.add(imageLabel);
                    imgPanel.add(titleLabel);

                    imgPanel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            new ArtworkDetailsFrame(artwork).setVisible(true);
                        }
                    });

                    panel.add(imgPanel);
                    panel.revalidate();
                }, Throwable::printStackTrace);
    }
}

