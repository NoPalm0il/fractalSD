package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BurningShip extends Fractal {
    @Override
    public int color(double re, double im, int i) {
        double zr = 0, zi = 0, nz;
        while (i > 0) {
            if (zr * zr + zi * zi > 4) {
                break;
            }

            nz = zr * zr - zi * zi + re;
            zi = Math.abs(2 * zi * zr) - im;
            zr = Math.abs(nz);
            i--;
        }
        return i;
    }

    @Override
    public int color(BigDecimal re, BigDecimal im, int i) {
        BigDecimal zr = BigDecimal.ZERO.setScale(20, RoundingMode.CEILING), zi = BigDecimal.ZERO.setScale(20, RoundingMode.CEILING), nz;
        while (i > 0) {
            if (zr.multiply(zr).add(zi.multiply(zi)).compareTo(new BigDecimal("4.0")) > 0) {
                break;
            }

            nz = zr.multiply(zr).subtract(zi.multiply(zi)).add(re).setScale(20, RoundingMode.CEILING);
            zi = zi.multiply(zr).multiply(new BigDecimal("2.0")).abs().subtract(im).setScale(20, RoundingMode.CEILING);
            zr = nz.abs();
            i--;
        }
        return i;
    }
}
