package src.logica;
import src.mapa.Posicion;
 
public class Bandera extends Entidad {
 
    private final Jugador jugador;
    private final java.awt.Color color;
 
    public Bandera(int id, Posicion posicion, Jugador jugador, java.awt.Color color) {
        super(id, TipoEntidad.BANDERA, posicion);
        this.jugador = jugador;
        this.color = color;
    }
 
    public Jugador getJugador() {
        return jugador;
    }
 
    public java.awt.Color getColor() {
        return color;
    }
 
    public boolean fueTocadaPor(Animal animal) {
        if (animal == null) {
            return false;
        }
        return this.getPosicion().equals(animal.getPosicion());
    }
}