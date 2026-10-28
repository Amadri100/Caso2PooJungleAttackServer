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
        if (animal == null || !animal.estaVivo()
                || this.getPosicion() == null || animal.getPosicion() == null
                || this.getPosicion().getPunto() == null
                || animal.getPosicion().getPunto() == null) {
            return false;
        }
        double diferenciaX = this.getPosicion().getPunto().getX() - animal.getPosicion().getPunto().getX();
        double diferenciaY = this.getPosicion().getPunto().getY() - animal.getPosicion().getPunto().getY();
        return Math.hypot(diferenciaX, diferenciaY) <= src.utilidades.Constantes.ENCOUNTER_RADIUS;
    }
}