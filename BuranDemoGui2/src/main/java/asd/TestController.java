package asd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Buran.
 *
 * @author: ${USER} Date: 12.08.13 Time: 23:52
 */
public class TestController {
    public VBox root;

    @FXML
    public void print() {
        for (int i = 0; i < 5; i++) {
            try {
                printX();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings |
                // File Templates.
            }
        }
    }

    @FXML
    public void printX() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dings/infoPanel.fxml"));
        //fxmlLoader.setRoot(this);
        //final InfoPanelController controller = new InfoPanelController();
        //fxmlLoader.setController(controller);

        System.out.println("Hallo Welt");
        //fxmlLoader.load();
        root.getChildren().add((Node) fxmlLoader.load());
        final InfoPanelController controller = fxmlLoader.getController();
        controller.infoTextProperty().setValue("Ich neuer eintrag sein");

    }
}
