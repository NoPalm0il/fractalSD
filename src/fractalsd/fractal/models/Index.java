package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

import java.util.ArrayList;
import java.util.Arrays;

public class Index {
    public ArrayList<Fractal> fractals;

    //Edit me, default classes
    private Fractal[] defaultFractals = {
            new BurningShip(),
            new Julia(),
            new Mandelbrot()
    };

    public Index() {
        fractals = new ArrayList(Arrays.asList(defaultFractals));
    }
}
