//Автор: Залесский А.А., группа 821701
package compressorPackage.compressor;
// класс конфигурации. В нем пользователь может задавать размеры блока, количество нейронов на втором слое,
// максимальную допустимую ошибку, шаг обучения(если он неадаптивный)
public class Config {

    public static final String IMAGE_NAME = "c19";
    public static final String IMAGE_FORMAT = "jpg";
    public static final String IN_IMAGE_PATH = System.getProperty("user.dir") + "\\image\\imageIn\\" + IMAGE_NAME + "." + IMAGE_FORMAT;
    public static final String OUT_IMAGE_PATH = System.getProperty("user.dir") + "\\image\\resultImage\\" + IMAGE_NAME + "\\resultImage." + IMAGE_FORMAT;
    public static final String OUT_INFO_PATH = System.getProperty("user.dir") + "\\image\\resultImage\\" + IMAGE_NAME + "\\info.txt";

    public static final double MAX_ERROR = 3000;
    public static final int NEURONS_NUMBER_ON_SECOND_LAYER = 32;
    public static final double LEARNING_RATE = 0.0001; //В данном варианте используется адаптивный шаг, для использования заранее заданного шага см. класс NeuralNetwork, метод changeWeights()
    public static final int BLOCK_WIDTH = 8;
    public static final int BLOCK_HEIGHT = 8;
}
