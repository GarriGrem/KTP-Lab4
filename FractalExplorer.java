import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;


/**
 * This class provides the common interface and operations for fractal
 * generators that can be viewed in the Fractal Explorer.
 */
public class FractalExplorer {
    /** Целое число "размер экрана, которое является шириной и высотой отображения **/
    private int displaySize;

    /**
     * Ссылка JImageDisplay для обновления отображения в разных методах
     * в процессе вычисления фрактала.
     */
    private JImageDisplay display;

    /**
     * Объект FractalGenerator. Ипользуется ссылка на базовый класс
     * для отображения других видов фракталов в будущем.
     */
    private FractalGenerator fractal;

    /**
     * Объект Rectangle2D.Double, указывающий диапазон
     * комплексной плоскости, которая выводится на экран.
     */
    private Rectangle2D.Double range;

    /**
     * Конструктор принимает значение размера отображения в качестве аргумента,
     * затем сохраняет это значение в соответствующем поле, а также
     * инициализирует объекты диапазона и фрактального генератора.
     */
    public FractalExplorer(int size) {
        /** Устанавливает размер отображения **/
        displaySize = size;

        /** Инициализация объектов диапазона и фрактального генератора. **/
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);

    }

    /**
     * Метод инициализирует графический интерфейс Swing: JFrame, содержащий объект
     * JImageDisplay и кнопку для сброса отображения.
     */
    public void createAndShowGUI()
    {
        /** Операция для использования
         *  java.awt.BorderLayout для его содержимого.  **/
        display.setLayout(new BorderLayout());
        JFrame myframe = new JFrame("Fractal Explorer");

        /**
         * Установка объекта отображения по центру
         */
        myframe.add(display, BorderLayout.CENTER);

        /** Создание объекта для кнопки resetButton с строковым параметром
         * для текста внутри кнопки */
        JButton resetButton = new JButton("Reset Display");

        /** Создание экземпляра класса ResetHandler для сброса
         * отображения и добавдение к обработчику события */
        ResetHandler handler = new ResetHandler();
        resetButton.addActionListener(handler);

        /** Установка кнопки resetButton на позицию SOUTH */
        myframe.add(resetButton, BorderLayout.SOUTH);

        /** Экземпляр MouseHandler на компонент фрактального дисплея. */
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        /** Операция закрытия окна по умолчанию. */
        myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         * Операции правильно разметят содержимое окна, сделают его
         * видимым, и затем запретят изменение размеров окна.
         */
        myframe.pack();
        myframe.setVisible(true);
        myframe.setResizable(false);
    }

    /**
     * Вспомогательный метод с модификатором доступа private для отображения фрактала.
     * Этот метод циклически проходит через каждый пиксель на экране и вычисляет
     * количество итераций для соответствующих координат в области отображения фрактала.
     * Если число итерациц равно -1, цвет пикселя устанавливается на черный.
     * В противном случае выбирается значение на основе количества итераций.
     * Обновлется отображение цветом для каждого пикселя и перекрашивается
     * JImageDisplay, когда все пиксели будут нарисованы.
     */
    private void drawFractal()
    {
        /** Проход через каждый пиксель на отображении. */
        for (int x=0; x<displaySize; x++){
            for (int y=0; y<displaySize; y++){

                /**
                 * Поиск соответствующих координат xCoord и yCoord
                 * в области отображения фрактала.
                 */
                double xCoord = fractal.getCoord(range.x,
                        range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y,
                        range.y + range.height, displaySize, y);

                /**
                 * Вычисление количества итераций для коодинат в
                 * области отображения фрактала.
                 */
                int iteration = fractal.numIterations(xCoord, yCoord);

                /** Если число итераций равно -1, установить пиксель
                 * в черынй цвет. */
                if (iteration == -1){
                    display.drawPixel(x, y, 0);
                }

                else {
                    /**
                     * В противном случае выбрать значение оттенка
                     * в зависимости от количества итераций.
                     */
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    /** Обновление отображения с цветом для каждого пикселя. */
                    display.drawPixel(x, y, rgbColor);
                }
            }
        }
        /**
         * Когда все пиксели будут нарисованы, перекрасить JImageDisplay
         * в соответсвии с текущим содержимым его изображения.
         */
        display.repaint();
    }

    /**
     * Внутренний класс для обработки событий ActionListener от кнопки сброса.
     */
    private class ResetHandler implements ActionListener
    {
        /**
         * Обработчик сбрасывает диапазон к начальному, заданному генератором,
         * а затем рисует фрактал.
         */
        public void actionPerformed(ActionEvent e)
        {
            fractal.getInitialRange(range);
            drawFractal();
        }
    }

    /**
     * Внутренний класс для обработки событий MouseListener с дисплея.
     */
    private class MouseHandler extends MouseAdapter
    {
        /**
         * Когда обработчик получает событие щелчка мышью, он отображает
         * пиксельные координаты щелчка в отображаемой области фрактала,
         * а затем вызывает метод recenterAndZoomRange() генератора с координатами,
         * по которым щелкнули, и масштабом 0,5.
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            /** Получить координату x области отображения щелчка мыши. */
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,
                    range.x + range.width, displaySize, x);

            /** Получить координату y области отображения щелчка мыши. */
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
                    range.y + range.height, displaySize, y);

            /**
             * Вызов метода recenterAndZoomRange() генератора
             * с координатами, по которым щелкнули, и масштабом 0,5.
             */
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            /**
             * Перерисовка фрактала после изменения отобрадаемой области.
             */
            drawFractal();
        }
    }

    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}