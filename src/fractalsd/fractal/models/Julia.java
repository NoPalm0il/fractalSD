package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Julia extends Fractal {
    @Override
    public int color(double re, double im, int i) {
        double zr = re, zi = im, nz;
        while (i > 0 && zr * zr + zi * zi < 4) {
            nz = zr * zr - zi * zi - 0.7;
            zi = 2 * zr * zi + 0.27015;
            zr = nz;
            i--;
        }

        return i;
    }

    @Override
    public int color(BigDecimal re, BigDecimal im, int i) {
        MathContext mc = new MathContext(20, RoundingMode.CEILING);
        BigDecimal zr = re.setScale(20, RoundingMode.CEILING);
        BigDecimal zi = im.setScale(20, RoundingMode.CEILING);
        BigDecimal nz;


        while (i > 0 && zr.multiply(zr,mc).add(zi.multiply(zi,mc), mc).setScale(20, RoundingMode.CEILING).compareTo(new BigDecimal("4.0")) < 0) {
            nz = zr.multiply(zr,mc).subtract(zi.multiply(zi,mc), mc).subtract(new BigDecimal("0.7"),mc).setScale(20, RoundingMode.CEILING);
            zi = zr.multiply(zi,mc).multiply(new BigDecimal("2.0"),mc).add(new BigDecimal("0.27015"),mc).setScale(20, RoundingMode.CEILING);
            zr = nz.setScale(20, RoundingMode.CEILING);
            i--;
        }
        return i;
    }
}
