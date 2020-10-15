package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

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
}
