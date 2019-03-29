package com.abner.main;

import java.awt.AlphaComposite;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class WaterMark {

    public void showFiles(File file, String watermark, String output_image) {
        File aFile[] = file.listFiles();

        int i = 0;
        for (int j = aFile.length; i < j; i++) {

            File files = aFile[i];
            File originalFile = new File(file + "\\" + files.getName());
            System.out.println(files.getName());
            File mark = new File(watermark);

            //Output file
            int min = 1000, max = 9999;
            int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);

            String filename = "IMG-" + randomNum;
            File outputFile = new File(output_image + "\\" + filename + ".jpg");

            addMark(mark, "png", originalFile, outputFile);
        }
    }

    public void addMark(File mark, String type, File file, File dest) {
        try {
            BufferedImage image = ImageIO.read(file);
            BufferedImage overlay = resize(ImageIO.read(mark), 150, 150);

            int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
            BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);

            Graphics2D w = (Graphics2D) watermarked.getGraphics();
            w.drawImage(image, 0, 0, null);
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
            w.setComposite(alphaChannel);

            //position watermark
            int centerX = image.getWidth() / 2;
            int centerY = image.getHeight() / 2;

            w.drawImage(overlay, centerX, centerY, null);
            ImageIO.write(watermarked, type, dest);

            w.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }

}
