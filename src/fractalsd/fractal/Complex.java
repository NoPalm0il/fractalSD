package fractalsd.fractal;

public class Complex {
    public double re;
    public double im;

    public Complex() {
        this.re = 0;
        this.im = 0;
    }

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getIm() {
        return im;
    }

    public void setIm(double im) {
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public void setRe(double re) {
        this.re = re;
    }

    public double modulus() {
        return Math.sqrt(this.re * re + this.im * im);
    }

    public Complex multiply(Complex z) {
        Complex result = new Complex();
        result.re = this.re * z.re - this.im * z.im;
        result.im = this.re * z.im + this.im * z.re;
        return result;
    }

    public Complex sum(Complex z) {
        Complex result = new Complex();
        result.re = this.re + z.re;
        result.im = this.im + z.im;

        return result;
    }
}
