package src.utilidades;
import java.lang.Math;
public class Punto {
    private double x;
    private double y;
    public Punto(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public boolean igual(Punto punto) {
        return this.x == punto.getX() && this.y == punto.getY();
    }

    public double distanciaCon(Punto punto) {
       return Math.sqrt(Math.pow(this.getX() - punto.getX(), 2) + Math.pow(this.getY() - punto.getY(), 2));
    }
}