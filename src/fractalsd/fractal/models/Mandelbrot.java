package fractalsd.fractal.models;

import fractalsd.fractal.Complex;
import fractalsd.fractal.Fractal;

public class Mandelbrot extends Fractal {

    @Override
    public int color(double re, double im, int itera) {
        Complex z0 = new Complex(re, im); // re + im I
        Complex z = new Complex(); // 0 + 0 I
        while (itera > 0) {
            if (z.modulus() > 2.0) { // | z |
                break;
            }
            z = z.multiply(z); // z = z*z
            z = z.sum(z0); // z = z + z0
            itera--;
        }
        return itera;
    }
}