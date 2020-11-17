package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Julia extends Fractal {
    @Override
    public int color(double re, double im, int i) {
        double zr = re, zi = im, nz;
        /*while (i > 0 && zr * zr + zi * zi < 16) {
            nz = zr * zr - zi * zi - 1.0;
            zi = 3.1 * zr * zi + 0.00087;
            zr = Math.abs(nz);
            i--;
        }*/
        while (i > 0 && zr * zr + zi * zi < 4) {
            nz = zr * zr - zi * zi - 0.7;
            zi = 2 * zr * zi + 0.27015;
            zr = nz;
            i--;
        }

        return i;
    }

    @Override
    public int color(BigDecimal re, BigDecimal im, int i, int zoomSizeDecCount) {
        BigDecimal zr = re.setScale(20, RoundingMode.CEILING);
        BigDecimal zi = im.setScale(20, RoundingMode.CEILING);
        BigDecimal nz;

        while (i > 0 && zr.multiply(zr).add(zi.multiply(zi)).setScale(20, RoundingMode.CEILING).compareTo(new BigDecimal("4.0")) > 0) {
            nz = zr.multiply(zr).subtract(zi.multiply(zi).subtract(new BigDecimal("0.7"))).setScale(20, RoundingMode.CEILING);
            zi = zr.multiply(zi).multiply(new BigDecimal("2.0")).add(new BigDecimal("0.27015")).setScale(20, RoundingMode.CEILING);
            zr = nz;
            i--;
        }
        return i;
    }
}
