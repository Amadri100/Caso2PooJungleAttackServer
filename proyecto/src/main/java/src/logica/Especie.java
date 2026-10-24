package src.logica;

public class Especie {

    private final TipoEspecie tipo;

    public Especie(TipoEspecie tipo) {
        this.tipo = tipo;
    }

    public TipoEspecie getTipo() { return tipo; }
}