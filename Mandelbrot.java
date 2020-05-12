import java.awt.geom.Rectangle2D;

/**
 * This class is a subclass of FractalGenerator.  It is used to compute the
 * Mandelbrot fractal.
 */
public class Mandelbrot extends FractalGenerator
{
    /**
     * Константа с максимальным количеством итераций.
     */
    public static final int MAX_ITERATIONS = 2000;

    /**
     * Метод позволяет генератору фракталов определить наиболее
     * "интересную" область комплексной плоскости для конкретного фрактала.
     * Методу передается прямоугольный объект, и метод изменяет
     * поля прямоугольника для отображения правильного начального
     * диапазона для фрактала.
     */
    public void getInitialRange(Rectangle2D.Double range)
    {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    /**
     * Метод реализует итеративную функцию для фрактала Мандельброта.
     */
    public int numIterations(double x, double y)
    {
        /** Начало итераций с 0. */
        int iteration = 0;
        /** Инициализация переменных для действительной и мнимой частей */
        double zreal = 0;
        double zimaginary = 0;

        /**
         * Вычисление Zn = Zn-1^2 + c, где значения - это комплексные числа, представленные
         * действительной и мнимой частью, Z0=0, а c это конкретная точка фрактала
         * который мы отображаем (заданный по x и y). Это повторяется до тех пор,
         * пока Z^2 > 4 (модуль Z больше, чем 2) или не будет достигнуто
         * максимальное количество итераций.
         */
        while (iteration < MAX_ITERATIONS &&
                zreal * zreal + zimaginary * zimaginary < 4)
        {
            double zrealUpdated = zreal * zreal - zimaginary * zimaginary + x;
            double zimaginaryUpdated = 2 * zreal * zimaginary + y;
            zreal = zrealUpdated;
            zimaginary = zimaginaryUpdated;
            iteration += 1;
        }

        /**
         * Если алгоритм дошёл до значения MAX_ITERATIONS,
         * возвращается -1, чтобы показать, что точка не выходит за границы.
         */
        if (iteration == MAX_ITERATIONS)
        {
            return -1;
        }

        return iteration;
    }

}
