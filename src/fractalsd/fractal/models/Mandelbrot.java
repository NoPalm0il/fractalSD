package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Mandelbrot extends Fractal {

    @Override
    public int color(double re, double im, int i) {
        double zr = 0.0, zi = 0.0, nz;
        while (i > 0) {
            //z.modulus
            if (zr * zr + zi * zi > 4.0) { // | z |
                break;
            }
            //z = z.multiply(z); // z = z*z
            nz = zr * zr - zi * zi + re;
            zi = 2 * zr * zi + im;
            zr = nz;

            //z = z.sum(z0); // z = z + z0
            i--;
        }
        return i;
    }

    @Override
    public int color(BigDecimal re, BigDecimal im, int i, int zoomSizeDecCount) {
        MathContext mc = new MathContext(zoomSizeDecCount, RoundingMode.CEILING);
        BigDecimal zr = BigDecimal.ZERO.setScale(zoomSizeDecCount, RoundingMode.CEILING),
                zi = BigDecimal.ZERO.setScale(zoomSizeDecCount, RoundingMode.CEILING), nz;
        while (i > 0) {
            BigDecimal mul = zr.multiply(zr, mc).setScale(zoomSizeDecCount, RoundingMode.CEILING);
            if (mul
                    .add(zi.multiply(zi, mc), mc).setScale(zoomSizeDecCount, RoundingMode.CEILING)
                    .compareTo(new BigDecimal("4.0")) > 0)
                break;

            nz = mul
                    .subtract(zi.multiply(zi, mc), mc).setScale(zoomSizeDecCount, RoundingMode.CEILING)
                    .add(re, mc).setScale(zoomSizeDecCount, RoundingMode.CEILING);

            zi = zr.multiply(zi.multiply(new BigDecimal("2.0"), mc), mc).setScale(zoomSizeDecCount, RoundingMode.CEILING)
                    .add(im, mc).setScale(zoomSizeDecCount, RoundingMode.CEILING);

            zr = nz.setScale(zoomSizeDecCount, RoundingMode.CEILING);
            i--;
        }
        return i;
    }

}