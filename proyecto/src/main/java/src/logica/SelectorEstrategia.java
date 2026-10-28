package src.logica;

import java.util.List;
import src.utilidades.GeneradorAleatorio;

public class SelectorEstrategia {
    private final List<EstrategiaBot> estrategias = List.of(
            new EstrategiaDirecta(),
            new EstrategiaCercana(),
            new EstrategiaAleatoria());

    public EstrategiaBot elegir() {
        return GeneradorAleatorio.getInstance().elegir(estrategias);
    }
}
