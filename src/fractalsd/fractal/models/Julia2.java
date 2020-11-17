package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
    public int color(BigDecimal re, BigDecimal im, int i) {
        BigDecimal zr = re.setScale(20, RoundingMode.CEILING), zi = im.setScale(20, RoundingMode.CEILING), nz;

        while (i > 0 && (zr.multiply(zr).add(zi.multiply(zi)).setScale(20, RoundingMode.CEILING).compareTo(new BigDecimal("16.0"))) < 0){
            nz = zr.multiply(zr).subtract(zi.multiply(zi)).subtract(new BigDecimal("1.0")).setScale(20, RoundingMode.CEILING);
            zi = zr.multiply(zi).multiply(new BigDecimal("3.1")).add(new BigDecimal("0.00087")).setScale(20, RoundingMode.CEILING);
            zr = nz;
            i--;
        }
        return i;
    }
}