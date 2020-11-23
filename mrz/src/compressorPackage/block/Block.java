// Автор класса: Залесский А.А., группа 821701
package compressorPackage.block;

import org.ejml.simple.SimpleMatrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
//Блок (прямоугольник)
public class Block {
    private BufferedImage image;
    private int x;
    private int y;
    private SimpleMatrix refVector;

    public void genRefVector() {
        ArrayList<Double> refVector_ = new ArrayList<>();

        for (int j = 0; j < image.getWidth(); j++) {
            for (int k = 0; k < image.getHeight(); k++) {
                int RGB = image.getRGB(j, k);
                // по формуле (1) из методички
                // (RGB & 0xff0000) >> 16: достаем красную компоненту из RGB (источник: StackOverFlow)
                // аналогично достаем зеленую и синию компоненты
                refVector_.add(2 * (double) ((RGB & 0xff0000) >> 16) / 255 - 1); //R
                refVector_.add(2 * (double) ((RGB & 0xff00) >> 8) / 255 - 1); //G
                refVector_.add(2 * (double) (RGB & 0xff) / 255 - 1); //B
            }
        }
        double[][] values = new double[1][refVector_.size()];
        for (int i = 0; i < refVector_.size(); i++) {
            values[0][i] = refVector_.get(i);
        }
        refVector = new SimpleMatrix(values);
    }

    public void genNewRGB() {
        refVector = (refVector.plus(1)).scale(255 / 2.0);
        int i = 0;
        for (int j = 0; j < image.getWidth(); j++) {
            for (int k = 0; k < image.getHeight(); k++) {
                Color newColor = new Color(Math.max(Math.min((int) refVector.get(i++), 255), 0),
                                           Math.max(Math.min((int) refVector.get(i++), 255), 0),
                                           Math.max(Math.min((int) refVector.get(i++), 255), 0));
                image.setRGB(j, k, newColor.getRGB());
            }
        }
    }


    public BufferedImage getImage() {
        return image;
    }

    public SimpleMatrix getRefVector() {
        return refVector;
    }

    public void setRefVector(SimpleMatrix refVector) {
        this.refVector = refVector;
    }

    public int getRefVectorSize() {
        return refVector.getNumElements();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Block(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }
}
