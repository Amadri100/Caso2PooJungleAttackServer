package src.mensajes;

// Cliente -> Servidor
// Mismo formato que Partida.mandarAtaque(A, B): int[]{idPack, idAnimal}.
public class MensajeAttack extends MensajeJugador {
    private int[] atacante;
    private int[] enemigo;

    // Constructor para Jackson
    protected MensajeAttack() {
        super(TipoMensaje.ATTACK, null);
    }

    public MensajeAttack(String nombre, int[] atacante, int[] enemigo) {
        super(TipoMensaje.ATTACK, nombre);
        this.atacante = atacante;
        this.enemigo = enemigo;
    }

    public int[] getAtacante() {
        return atacante;
    }

    public int[] getEnemigo() {
        return enemigo;
    }
}
