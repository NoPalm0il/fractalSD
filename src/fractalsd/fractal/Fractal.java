package fractalsd.fractal;

import java.math.BigDecimal;

/**
 * foi criada esta classe abstrata para que seja possivel definir os outros fratais
 * e assim criar tipos do mesmo.
 * Sendo facilitado o uso e a criacao de objetos
 */
public abstract class Fractal {
    /**
     * Gera o numero de colisoes de um determinado fratal
     *
     * @param re numero real do grafico (x)
     * @param im numero imaginario do grafico (y)
     * @param i  iteracoes
     * @return retorna as colisoes
     */
    public abstract int color(double re, double im, int i);

    /**
     * Gera o numero de colisoes de um determinado fratal
     *
     * @param re               numero real (em BigDecimal) do grafico (x)
     * @param im               numero imaginario (em BigDecimal) do grafico (y)
     * @param i                iteracoes
     * @param zoomSizeDecCount numero de casas decimais do zoom
     * @return retorna as colisoes
     */
    public abstract int color(BigDecimal re, BigDecimal im, int i, int zoomSizeDecCount);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}

