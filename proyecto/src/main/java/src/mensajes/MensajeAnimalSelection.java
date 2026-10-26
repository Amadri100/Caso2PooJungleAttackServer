package src.mensajes;

import java.util.List;

import src.logica.TipoEspecie;

// Servidor -> Cliente
public class MensajeAnimalSelection extends MensajeServidor {
    private List<TipoEspecie> especiesDisponibles;
    private TipoEspecie seleccionOponente;

    // Constructor para Jackson
    protected MensajeAnimalSelection() {
        super(TipoMensaje.ANIMAL_SELECTION);
    }

    public MensajeAnimalSelection(List<TipoEspecie> especiesDisponibles, TipoEspecie seleccionOponente) {
        super(TipoMensaje.ANIMAL_SELECTION);
        this.especiesDisponibles = especiesDisponibles;
        this.seleccionOponente = seleccionOponente;
    }

    public List<TipoEspecie> getEspeciesDisponibles() {
        return especiesDisponibles;
    }

    public TipoEspecie getSeleccionOponente() {
        return seleccionOponente;
    }
}
