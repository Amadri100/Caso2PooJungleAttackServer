package src.mensajes;

import src.logica.TipoEspecie;

// Cliente -> Servidor
public class MensajeSelectAnimal extends MensajeJugador {
    private TipoEspecie especie;

    // Constructor para Jackson
    protected MensajeSelectAnimal() {
        super(TipoMensaje.SELECT_ANIMAL, null);
    }

    public MensajeSelectAnimal(String nombre, TipoEspecie especie) {
        super(TipoMensaje.SELECT_ANIMAL, nombre);
        this.especie = especie;
    }

    public TipoEspecie getEspecie() {
        return especie;
    }
}
