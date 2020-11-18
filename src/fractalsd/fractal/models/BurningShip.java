package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * classe do fractal Burning Ship
 */
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
            BigDecimal mul = zr.multiply(zr, mc).setScale(zoomSizeDecCount, RoundingMode.CEILING);
            BigDecimal mulzi = zi.multiply(zi, mc).setScale(zoomSizeDecCount, RoundingMode.CEILING);
            if (mul
                    .add(mulzi, mc).setScale(zoomSizeDecCount, RoundingMode.CEILING)
                    .compareTo(new BigDecimal("4.0")) > 0) {
                break;
            }

            nz = mul
                    .subtract(mulzi, mc).setScale(zoomSizeDecCount, RoundingMode.CEILING)
                    .add(re, mc).setScale(zoomSizeDecCount, RoundingMode.CEILING);
            zi = zi.multiply(zr, mc).setScale(zoomSizeDecCount, RoundingMode.CEILING)
                    .multiply(new BigDecimal("2.0"), mc).setScale(zoomSizeDecCount, RoundingMode.CEILING)
                    .abs(mc).setScale(zoomSizeDecCount, RoundingMode.CEILING).subtract(im, mc).setScale(zoomSizeDecCount, RoundingMode.CEILING);
            zr = nz.abs(mc).setScale(zoomSizeDecCount, RoundingMode.CEILING);
            i--;
        }
        return i;
    }
}
