package aharon.tours;

import javax.swing.*;
import java.awt.*;

public class ToursFrame extends JFrame {
    private JPanel[] panels = new JPanel[18];   // an array of panels- one for each tour
    private ToursController controller;

    public ToursFrame() {
        setTitle("Tours");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);
        setLayout(new GridLayout(6, 3));

        for (int i = 0; i < panels.length; i++) {       // creates the panels
            panels[i] = new JPanel();
            panels[i].setLayout(new BoxLayout(panels[i], BoxLayout.Y_AXIS));    // makes each panel a vertical box layout
            JLabel imageLabel = new JLabel();
            JLabel titleLabel = new JLabel();
            panels[i].add(imageLabel);  // adds tour image to the panel
            panels[i].add(titleLabel);  // adds tour title to the panel
            add(panels[i]);
        }

        ToursService service = new ToursServiceFactory().create();
        TourDetailsController detailsController = new TourDetailsController(service);
        controller = new ToursController(service, panels, detailsController);
        controller.display();
    }

    public static void main(String[] args) {
        new ToursFrame().setVisible(true);
    }
}
