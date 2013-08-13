package asd;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * Buran.
 *
 * @author: ${USER} Date: 12.08.13 Time: 23:52
 */
public class InfoPanelController {

    @FXML
    public Text textField;

    public StringProperty infoTextProperty() {
        return this.textField.textProperty();
    }

    @FXML
    public void onHyperlinkClick() {
        System.out.println("Hyperlink clicked");
    }

}
