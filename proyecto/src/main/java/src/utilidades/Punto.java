package src.utilidades;

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
}