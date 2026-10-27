package src.enviables;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.util.Objects;
import src.logica.Animal;
import src.logica.Bandera;
import src.logica.Cafe;
import src.logica.Entidad;
import src.logica.TipoEntidad;

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
        this.id = id;
        this.tipo = Objects.requireNonNull(tipo, "El tipo de entidad es obligatorio.");
        this.posicion = posicion;
    }

    public static EntidadEnviar desde(Entidad entidad) {
        Objects.requireNonNull(entidad, "La entidad es obligatoria.");
        if (entidad instanceof Animal animal) {
            return AnimalEnviar.desde(animal);
        }
        if (entidad instanceof Cafe cafe) {
            return CafeEnviar.desde(cafe);
        }
        if (entidad instanceof Bandera bandera) {
            return BanderaEnviar.desde(bandera);
        }
        throw new IllegalArgumentException(
                "No existe conversión enviable para la entidad " + entidad.getClass().getName()
                        + " (tipo " + entidad.getTipo() + ").");
    }

    public int getId() {
        return id;
    }

    public TipoEntidad getTipo() {
        return tipo;
    }

    public PosicionEnviar getPosicion() {
        return posicion;
    }
}
