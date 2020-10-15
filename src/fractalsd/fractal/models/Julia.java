package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

public class Julia extends Fractal {
    @Override
    public int color(double re, double im, int i) {
        double zr = re, zi = im, nz;
        while (i > 0 && zr * zr + zi * zi < 16) {
            nz = zr * zr - zi * zi - 1.0;
            zi = 3.1 * zr * zi + 0.00087;
            zr = Math.abs(nz);
            i--;
        }
        return i;
    }
}
