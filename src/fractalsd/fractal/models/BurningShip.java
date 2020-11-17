package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

import java.math.BigDecimal;
import java.math.MathContext;
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
    public int color(BigDecimal re, BigDecimal im, int i, int zoomSizeDecCount) {
        MathContext mc = new MathContext(zoomSizeDecCount, RoundingMode.CEILING);
        BigDecimal zr = BigDecimal.ZERO.setScale(zoomSizeDecCount, RoundingMode.CEILING),
                zi = BigDecimal.ZERO.setScale(zoomSizeDecCount, RoundingMode.CEILING), nz;
        while (i > 0) {
            if (zr.multiply(zr, mc).add(zi.multiply(zi, mc), mc).compareTo(new BigDecimal("4.0")) > 0) {
                break;
            }

            nz = zr.multiply(zr, mc).subtract(zi.multiply(zi, mc), mc).add(re, mc);
            zi = zi.multiply(zr, mc).multiply(new BigDecimal("2.0"), mc).abs(mc).subtract(im, mc);
            zr = nz.abs(mc);
            i--;
        }
        return i;
    }
}
