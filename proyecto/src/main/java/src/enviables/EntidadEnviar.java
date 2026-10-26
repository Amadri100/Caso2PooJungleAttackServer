package src.enviables;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import src.logica.*;

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "tipo", visible = true)
@JsonSubTypes({
        @Type(value = AnimalEnviar.class, name = "ANIMAL"),
        @Type(value = CafeEnviar.class, name = "CAFE"),
        @Type(value = BanderaEnviar.class, name = "BANDERA")
})
public abstract class EntidadEnviar {

    private int id;
    private TipoEntidad tipo;
    private PosicionEnviar posicion;

    public EntidadEnviar(int id, TipoEntidad tipo, PosicionEnviar posicion) {
    }

    public static EntidadEnviar desde(Entidad entidad) {
        return null;
    }

    public int getId() {
        return 0;
    }

    public TipoEntidad getTipo() {
        return null;
    }

    public PosicionEnviar getPosicion() {
        return null;
    }
}
