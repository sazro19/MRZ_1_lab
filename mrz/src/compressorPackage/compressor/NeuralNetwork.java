//Автор: Залесский А.А., группа 821701
package compressorPackage.compressor;

import compressorPackage.block.Block;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Random;

public class NeuralNetwork {
    private double maxError;
    private double learningRate; // шаг обучения задаваемый пользователем, в данном варианте не используется, так как шаг адаптивный(см. метод startTrain())
    private SimpleMatrix weightOfFirstLayer; // W
    private SimpleMatrix weightOfSecondLayer; // W'
    private ArrayList<Block> blocks;
    private static String info = "";

    private void startTrain() {
        int epoch = 1;
        while (true) {
            double MSE = 0;
            for (Block block : blocks) {
                SimpleMatrix X = new SimpleMatrix(block.getRefVector());
                SimpleMatrix Y = X.mult(weightOfFirstLayer); // по формуле (2) из методички
                SimpleMatrix XHatch = Y.mult(weightOfSecondLayer); // по формуле (3) из методички
                SimpleMatrix deltaX = XHatch.minus(X); // по формуле (4) из методички

                changeWeights(X, Y, deltaX);

                MSE += (deltaX.elementMult(deltaX)).elementSum();

            }
            System.out.println("Learning epoch " + epoch++ + ":\nTotal Error: " + MSE);
            if (MSE <= maxError) {
                learningSummary(weightOfFirstLayer, weightOfSecondLayer, MSE, epoch - 1);
                break;
            }
        }
    }

    private void changeWeights(SimpleMatrix X, SimpleMatrix Y, SimpleMatrix deltaX){
        // адаптивный шаг для корректировки весов первого и второго слоев по формулам из методички
        double adaptiveLearningRateForSecond = 1 / (Y.mult(Y.transpose())).get(0,0);
        double adaptiveLearningRateForFirst = 1 / (X.mult(X.transpose())).get(0, 0);

        // по формуле (6) из методички
        weightOfFirstLayer = weightOfFirstLayer.minus(
                ((X.transpose().scale(adaptiveLearningRateForFirst*0.1)).mult(deltaX)).mult(weightOfSecondLayer.transpose()));
        // по формуле (5) из методички
        weightOfSecondLayer = weightOfSecondLayer.minus(
                (Y.transpose().scale(adaptiveLearningRateForSecond*0.1)).mult(deltaX));

        //Корректировка весов с шагом, заданным пользователем (без адаптивного)

        // weightOfFirstLayer = weightOfFirstLayer.minus(
        // ((X.transpose().scale(learningRate)).mult(deltaX)).mult(weightOfSecondLayer.transpose())
        // );

        // weightOfSecondLayer = weightOfSecondLayer.minus(
        // (Y.transpose().scale(learningRate)).mult(deltaX)
        // );
    }

    public static void learningSummary(SimpleMatrix weightOfFirstLayer, SimpleMatrix weightOfSecondLayer,
                                       double error, int steps) {
        info = "Summary:" +
                "\n\tBlock height: " + Config.BLOCK_HEIGHT +
                "\n\tBlock width: " + Config.BLOCK_WIDTH +
                "\n\tNumber of hidden layer neurons: " + Config.NEURONS_NUMBER_ON_SECOND_LAYER +
                "\n\tLearning rate: " + Config.LEARNING_RATE +
                "\n\tLearning steps: " + steps +
                "\n\tFinal error: " + error +
                "\n\tFinal 1st layer weight matrix:\n\t\t" + weightOfFirstLayer +
                "\n\tFinal 2nd layer weight matrix:\n\t\t" + weightOfSecondLayer +
                "\n";
    }

    public ArrayList<Block> getResultBlocks() {
        for (Block block : blocks) {
            SimpleMatrix X = new SimpleMatrix(block.getRefVector());
            SimpleMatrix Y = X.mult(weightOfFirstLayer);
            SimpleMatrix XHatch = Y.mult(weightOfSecondLayer);
            block.setRefVector(XHatch);
            block.genNewRGB();
        }
        return blocks;
    }

    public NeuralNetwork(double maxError, int secondLayerNeuronsNumber, double learningRate,
                         int refVectorSize, ArrayList<Block> blocks) {
        this.maxError = maxError;
        weightOfFirstLayer = SimpleMatrix.random_DDRM(refVectorSize, secondLayerNeuronsNumber, -1, 1,
                                                      new Random());
        weightOfSecondLayer = weightOfFirstLayer.transpose();
        weightOfSecondLayer = weightOfSecondLayer.scale(0.1);
        this.blocks = blocks;
        this.learningRate = learningRate;

        startTrain();
    }

    public static String getInfo(){
        return info;
    }

    public static void setInfo(String newInfo){
        info = newInfo;
    }
}