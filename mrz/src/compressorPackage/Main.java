
package compressorPackage;

import compressorPackage.compressor.Compressor;
import compressorPackage.compressor.Config;
import compressorPackage.compressor.NeuralNetwork;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(Config.IN_IMAGE_PATH));
            System.out.println("Dimensions: " + image.getWidth() + " by " + image.getHeight());
        } catch (Exception e) {
            System.out.println("File read unsuccessful");
            e.printStackTrace();
        }

        Compressor compressor = new Compressor(Config.BLOCK_WIDTH, Config.BLOCK_HEIGHT, image);

        BufferedImage resultImage = compressor.getResultImage();
        try {
            Files.createDirectories(Paths.get(Config.OUT_IMAGE_PATH));
        } catch (IOException ignored) {
        }

        try {
            ImageIO.write(resultImage, Config.IMAGE_FORMAT, new File(Config.OUT_IMAGE_PATH));
        } catch (IOException e) {
            System.out.println("File write unsuccessful");
            e.printStackTrace();
        }

        try {
            File file = new File(Config.OUT_INFO_PATH);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            Writer writer = new BufferedWriter(outputStreamWriter);
            writer.write(NeuralNetwork.getInfo());
            writer.close();
        } catch (Exception ignored) { }
    }
}
