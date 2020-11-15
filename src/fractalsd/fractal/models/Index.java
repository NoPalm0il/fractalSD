package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

public class Index {
    public Fractal[] fractals;

    public Index() {
        fractals = new Fractal[]{
                new BurningShip(),
                new Mandelbrot(),
                new Julia()
        };
    }
}
