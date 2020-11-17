package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * classe do fractal Julia com c = -1.0 + 0.00087i
 */
public class Julia2 extends Fractal {
    @Override
    public int color(double re, double im, int i) {
        double zr = re, zi = im, nz;
        while (i > 0 && zr * zr + zi * zi < 16) {
            nz = zr * zr - zi * zi - 1.0;
            zi = 3.1 * zr * zi + 0.00087;
            zr = nz;
            i--;
        }

        return i;
    }

    @Override
    public int color(BigDecimal re, BigDecimal im, int i, int zoomSizeDecCount) {
        BigDecimal zr = re.setScale(zoomSizeDecCount, RoundingMode.CEILING), zi = im.setScale(zoomSizeDecCount, RoundingMode.CEILING), nz;

        while (i > 0 && (zr.multiply(zr).add(zi.multiply(zi)).setScale(zoomSizeDecCount, RoundingMode.CEILING).compareTo(new BigDecimal("16.0"))) < 0){
            nz = zr.multiply(zr).subtract(zi.multiply(zi)).subtract(new BigDecimal("1.0")).setScale(zoomSizeDecCount, RoundingMode.CEILING);
            zi = zr.multiply(zi).multiply(new BigDecimal("3.1")).add(new BigDecimal("0.00087")).setScale(zoomSizeDecCount, RoundingMode.CEILING);
            zr = nz;
            i--;
        }
        return i;
    }
}