package fractalsd.fractal;

//foi criada esta classe abestrata para que seja possivel definir os outros fratais
//e assim criar tipos de fratais, sendo facilitado o uso e a criacao de objetos
public abstract class Fractal {
    public abstract int color(double re, double im, int i);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
