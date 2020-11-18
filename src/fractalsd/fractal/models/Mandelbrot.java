package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Mandelbrot extends Fractal {

    /**
     * Gera o numero de colisoes do fratal de Mandelbroth optimizado
     *
     * @param re - numero real do grafico (x)
     * @param im - numero imaginario do grafico (y)
     * @param i  - iteracoes
     * @return - retorna as colisoes
     */
    @Override
    public int color(double re, double im, int i) {
        double x, x2 = 0.0, y, y2 = 0.0, w = 0.0;
        while (i > 0 && x2 + y2 <= 4) {
            x = x2 - y2 + re;
            y = w - x2 - y2 + im;
            x2 = x * x;
            y2 = y * y;
            w = (x + y) * (x + y);

            i--;
        }
        return i;
    }

    @Override
    public int color(BigDecimal re, BigDecimal im, int i, int zoomSizeDecCount) {
        MathContext mc = new MathContext(zoomSizeDecCount, RoundingMode.CEILING);
        BigDecimal x, y, x2 = BigDecimal.ZERO, y2 = BigDecimal.ZERO, w = BigDecimal.ZERO;
        BigDecimal cmp = new BigDecimal("4.0");
        while (i > 0 && x2.add(y2, mc).compareTo(cmp) <= 0) {
            x = x2.subtract(y2, mc).add(re, mc);
            y = w.subtract(x2, mc).subtract(y2, mc).add(im, mc);

            x2 = x.multiply(x, mc);
            y2 = y.multiply(y, mc);
            w = x.add(y, mc).multiply(x.add(y, mc), mc);

            i--;
        }
        return i;
    }

}