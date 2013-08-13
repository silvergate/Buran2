package com.dcrux.buran.demoGui.files;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Buran.
 *
 * @author: ${USER} Date: 12.08.13 Time: 23:52
 */
public class FileUploaderController implements EventHandler<DragEvent>, Initializable {
    @FXML
    public Text textField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textField.setOnDragDropped(this);
        //textField.setOnDragDetected(this);
        //textField.setOnMouseEntered(this);
        textField.setOnDragEntered(this);
        textField.setOnDragOver(this);
    }

    @Override
    public void handle(DragEvent event) {
        System.out.println("DRAG: " + event);
        event.acceptTransferModes(TransferMode.ANY);
        final Dragboard dragBoard = event.getDragboard();
        if (dragBoard != null) {
            final List<File> files = event.getDragboard().getFiles();
            if ((files != null) && (!files.isEmpty())) {
                for (final File file : files) {
                    System.out.println("FILE: " + file);
                }
            }
        }
        //event.
    }
}
