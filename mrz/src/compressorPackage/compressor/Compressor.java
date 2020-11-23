//Автор: Залесский А.А., группа 821701
package compressorPackage.compressor;

import compressorPackage.block.Block;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;

public class Compressor {

    private int blockWidth; //m
    private int blockHeight; //n

    private BufferedImage image;
    private ArrayList<Block> blocks = new ArrayList<>();

    private void genPixelBlocks() {
        Raster imageData = image.getData();
        double excessiveHeight = imageData.getHeight() % blockHeight;
        double excessiveWidth = imageData.getWidth() % blockWidth;
        int x, y;
        for (x = 0; x < imageData.getWidth() - excessiveWidth; x += blockWidth) {
            for (y = 0; y < imageData.getHeight() - excessiveHeight; y += blockHeight) {
                blocks.add(new Block(x, y, image.getSubimage(x, y, blockWidth, blockHeight)));
            }
            //создаем оставшийся прямоугольник, который не влез
            if (excessiveHeight != 0) {
                y = (int) (y - (blockHeight - excessiveHeight));
                blocks.add(new Block(x, y, image.getSubimage(x, y, blockWidth, blockHeight)));
            }
        }

        if (excessiveWidth != 0) {
            x = (int) (x - (blockWidth - excessiveWidth));
            for (y = 0; y < imageData.getHeight() - excessiveHeight; y += blockHeight) {
                blocks.add(new Block(x, y, image.getSubimage(x, y, blockWidth, blockHeight)));
            }

            if (excessiveHeight != 0) {
                y = (int) (y - (blockHeight - excessiveHeight));
                blocks.add(new Block(x, y, image.getSubimage(x, y, blockWidth, blockHeight)));
            }
        }
    }

    public BufferedImage getResultImage() {
        int refVectorSize = blocks.get(0).getRefVectorSize();
        NeuralNetwork neuralNetwork = new NeuralNetwork(Config.MAX_ERROR, Config.NEURONS_NUMBER_ON_SECOND_LAYER,
                                                        Config.LEARNING_RATE, refVectorSize, blocks);

        ArrayList<Block> newBlocks = neuralNetwork.getResultBlocks();

        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(),
                                                      BufferedImage.TYPE_INT_RGB);
        for (Block block : newBlocks) {
            resultImage.getGraphics().drawImage(block.getImage(), block.getX(),
                                                block.getY(), null);
        }
        // коэффициент сжатия по формуле (10) из методички
        double compressionQuotient = (refVectorSize * blocks.size()) / (double) ((refVectorSize + blocks.size())
                                      * Config.NEURONS_NUMBER_ON_SECOND_LAYER + 2);

        System.out.println("Done. Compression quotient: " + compressionQuotient);
        String temp = NeuralNetwork.getInfo();
        temp += "Compression quotient: " + compressionQuotient + "\n";
        NeuralNetwork.setInfo(temp);
        return resultImage;
    }

    public Compressor(int blockWidth, int blockHeight, BufferedImage image) {
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.image = image;
        genPixelBlocks();
        for (Block block : blocks) {
            block.genRefVector();
        }
    }
}