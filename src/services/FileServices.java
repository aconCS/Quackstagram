package services;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileServices {

    public File openFileChooser(String dialogTitle, String... extensions) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(dialogTitle);
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", extensions);
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}
