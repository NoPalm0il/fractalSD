package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

/**
 * classe que contem o array do tipo Fractal com os 4 fractais:
 *      - Burning Ship;
 *      - Mandelbrot;
 *      - Julia;
 *      - Julia (com coordenadas diferentes).
 */
public class Index {
    public Fractal[] fractals;

    public Index() {
        fractals = new Fractal[]{
                new BurningShip(),
                new Mandelbrot(),
                new Julia(),
                new Julia2()
        };
    }
}
