package aharon.tours;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class ToursController {

    private static final Logger logger = Logger.getLogger(
            ToursController.class.getName());        // logs the name of the class that has the error

    private ToursService service;
    private JPanel[] panels;
    private TourDetailsController detailsController;

    public ToursController(ToursService service, JPanel[] panels, TourDetailsController detailsController) {
        this.panels = panels;
        this.service = service;
        this.detailsController = detailsController;
    }


    public void display() {
        Disposable disposable = service.getTours()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(SwingUtilities::invokeLater))
                .subscribe(
                        (response) -> handleResponse(response),
                        Throwable::printStackTrace);
    }

    private void handleResponse(ToursResponse response) {
        for (int i = 0; i < panels.length; i++) {
            final Tour tour = response.data[i];
            final JPanel panel = panels[i];
            final JLabel imageLabel = (JLabel) panel.getComponent(0);
            final JLabel titleLabel = (JLabel) panel.getComponent(1);

            panel.putClientProperty("tour", tour);
            titleLabel.setText(tour.title); // set the title under the image

            downloadImage(tour, imageLabel);

            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    Tour tour = (Tour) panel.getClientProperty("tour");
                    if (tour != null) {
                        TourDetailsFrame detailsFrame = new TourDetailsFrame(tour, detailsController);
                        detailsFrame.setVisible(true);
                    }
                }
            });
        }

    }

    private void downloadImage(Tour tour, JLabel label) {
        Disposable disposable = Single.fromCallable((Callable<Image>) () -> {
                    logger.info("Downloading thumbnail " + tour.title);
                    URL url = new URL(tour.image);
                    return ImageIO.read(url);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(SwingUtilities::invokeLater))
                .subscribe(
                        (image) -> handleImage(label, image),
                        Throwable::printStackTrace);
    }

    private void handleImage(JLabel label, Image image) {
        int width = 180;
        int height = 120;
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaledImage));
    }
}


