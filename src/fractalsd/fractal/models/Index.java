package fractalsd.fractal.models;

import fractalsd.fractal.Fractal;

import java.util.ArrayList;
import java.util.Arrays;

public class Index {
    public ArrayList<Fractal> fractals;

    //Edit me, default classes
    private Fractal[] defaultFractals = {
            new Mandelbrot(),
            new Julia(),
            new BurningShip()
    };

    public Index() {
        fractals = new ArrayList(Arrays.asList(defaultFractals));
    }
}
