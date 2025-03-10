package services;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

    public static String getElapsedTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeOfNotification = LocalDateTime.parse(timestamp, formatter);
        LocalDateTime currentTime = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
        long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

        StringBuilder timeElapsed = new StringBuilder();
        if (daysBetween > 0) {
            timeElapsed.append(daysBetween).append(" day").append(daysBetween > 1 ? "s" : "");
        }
        if (minutesBetween > 0) {
            if (daysBetween > 0) {
                timeElapsed.append(" and ");
            }
            timeElapsed.append(minutesBetween).append(" minute").append(minutesBetween > 1 ? "s" : "");
        } else {
            timeElapsed.append("Just now");
        }

        return timeElapsed.toString();
    }
}
