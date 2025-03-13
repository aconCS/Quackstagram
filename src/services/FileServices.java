package services;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class FileServices {

    public static File openFileChooser(String dialogTitle, String... extensions) {
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

    public static String getElapsedTimestamp(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeOfNotification = LocalDateTime.parse(timestamp, formatter);
        LocalDateTime currentTime = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
        long hoursBetween = ChronoUnit.HOURS.between(timeOfNotification, currentTime) % 24;
        long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

        if (daysBetween == 0 && hoursBetween == 0 && minutesBetween == 0) {
            return "Just now";
        }

        StringBuilder timeElapsed = new StringBuilder();

        if (daysBetween > 0) {
            timeElapsed.append(daysBetween).append(" day").append(daysBetween > 1 ? "s" : "");
        }

        if (hoursBetween > 0) {
            if (!timeElapsed.isEmpty()) {
                timeElapsed.append(", ");
            }
            timeElapsed.append(hoursBetween).append(" hour").append(hoursBetween > 1 ? "s" : "");
        }

        if (minutesBetween > 0) {
            if (!timeElapsed.isEmpty()) {
                timeElapsed.append(" and ");
            }
            timeElapsed.append(minutesBetween).append(" minute").append(minutesBetween > 1 ? "s" : "");
        }

        return timeElapsed.toString();
    }

    public static ImageIcon createScaledIcon(String path, int width, int height) {
        ImageIcon imageIcon;
        try {
            BufferedImage originalImage = ImageIO.read(new File(path));
            if (originalImage == null) {
                System.out.println("Image not found at path: " + path);
                return null;
            }

            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bufferedScaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedScaledImage.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();

            BufferedImage croppedImage = bufferedScaledImage.getSubimage(0, 0, Math.min(bufferedScaledImage.getWidth(), width), Math.min(bufferedScaledImage.getHeight(), height));
            imageIcon = new ImageIcon(croppedImage);
        } catch (IOException ex) {
            System.out.println("Error reading image: " + ex.getMessage());
            return null;
        }
        return imageIcon;
    }

    public static ArrayList<String> getAllImageIds() {
        ArrayList<String> imageIDs = new ArrayList<>();
        File folder = new File("resources/img/uploaded/");
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    imageIDs.add(fileName.split("\\.")[0]);
                }
            }
        }
        return imageIDs;
    }
}
