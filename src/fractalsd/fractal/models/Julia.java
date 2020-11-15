package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

import java.math.BigDecimal;

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
    public int color(BigDecimal re, BigDecimal im, int i) {
        return 0;
    }
}
