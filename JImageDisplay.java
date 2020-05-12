import javax.swing.*;
import java.awt.image.*;
import java.awt.*;

/**
 * Класс насоедуется от javax.swing.JComponent
 */
class JImageDisplay extends JComponent
{
    /**
     * Экземпляр java.awt.image.BufferedImage
     * Управляет изображением, содержимое которого можно записать
     */
    private BufferedImage displayImage;

    /**
     * Конструктор принимает целочисленные значения ширины и высоты
     * и иницииализирует объект BufferedImage новым изображением с
     * этой шириной и высотой, и типом изображения TYPE_INT_RGB.
     * Тип определяет, как цвета каждого пикселя будут представлены
     * в изображении; значение TYPE_INT_RGB обозначает, что красные,
     * зеленые и синие компоненты имеют по 8 битов, представленные в формате
     * int  указанном порядке.
     */
    public JImageDisplay(int width, int height) {
        displayImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        /**
         * Конструктор также вызывает метод setPrefferedSize()
         * родительского класса с указанной шириной и высотой.
         * Значения передаются в объект java.awt.Dimension.
         * Таким образом, когда компонент будет включен в
         * пользовательский интерфейс, он отобразит на экране
         * все изображение
         */
        Dimension imageDimension = new Dimension(width, height);
        super.setPreferredSize(imageDimension);

    }
    /**
     * Переопределение метода paintComponent.
     * Вызывается метод суперкласса paintComponent(g)
     * Далее вызывается метод, который рисует изображение
     * в компоненте. Последний параметр null для
     * ImageObserver, так как его функциональность не требуется
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(displayImage, 0, 0, displayImage.getWidth(),
                displayImage.getHeight(), null);
    }
    /**
     * Устанавливает все пиксели изображения в черный цвет.
     */
    public void clearImage()
    {
        int[] blankArray = new int[getWidth() * getHeight()];
        displayImage.setRGB(0, 0, getWidth(), getHeight(), blankArray, 0, 1);
    }
    /**
     * Метод устанавливает пиксель в определенный цвет.
     */
    public void drawPixel(int x, int y, int rgbColor)
    {
        displayImage.setRGB(x, y, rgbColor);
    }
}
