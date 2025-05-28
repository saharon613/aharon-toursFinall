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
        // This will make a request for the ProductResponse on a separate Thread so that it wont have to wait for it to finish in order to continue
        Disposable disposable = service.getTours()
                // tells Rx to request the data on a background Thread
                .subscribeOn(Schedulers.io())
                // tells Rx to handle the response on Swing's main Thread- tells it to call the handleResponse method- and do it on the main thread
                .observeOn(Schedulers.from(SwingUtilities::invokeLater))
                //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                .subscribe(
                        (response) -> handleResponse(response), // if you have a response call the handle response method
                        Throwable::printStackTrace);        // if it doesn't get a response do this
    }

    private void handleResponse(ToursResponse response) {
        for (int i = 0; i < panels.length; i++) {
            final Tour tour = response.data[i];
            final JPanel panel = panels[i];
            final JLabel imageLabel = (JLabel) panel.getComponent(0);   // gets the first element of the panel - here its the image
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
                    //Thread.sleep(new Random().nextInt(5) * 1000);
                    URL url = new URL(tour.image);
                    return ImageIO.read(url);
                })
                .subscribeOn(Schedulers.io())
                // tells Rx to handle the response on Swing's main Thread- tells it to call the handleResponse method- and do it on the main thread
                .observeOn(Schedulers.from(SwingUtilities::invokeLater))
                //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                .subscribe(
                        (image) -> handleImage(label, image), // if you have a response call the handle response method
                        Throwable::printStackTrace);        // if it doesn't get a response do this
    }

    private void handleImage(JLabel label, Image image) {
        int width = 180;
        int height = 120;
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaledImage));
    }
}


