package src.mapa;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Mapa {
    private static volatile Mapa instance = null;
    private ArrayList<Zona> zonas;
    private HashMap<Nodo, ArrayList<Vecinos>> vecinos;
    private Mapa() {
        crearZonas();
        crearNodos();
        crearVecinos();
        crearMapaVecinos();
    }
    private void crearZonas() {}
    private void crearNodos() {}
    private void crearVecinos() {}
    private void crearMapaVecinos() {}

    public static Mapa getInstance() {
        if (instance == null) {
            synchronized (Mapa.class) {
                if (instance == null) {
                    instance = new Mapa();
                }
            }
        }
        return instance;
    }
    public ArrayList<Zona> getZonas() {
        return this.zonas;
    }
    
    public Ruta getRuta(Nodo origen, Nodo destino) {

        //Algoritmo Dijkstra

        Map<Nodo, Double> distancia = new HashMap<>(); //Distancia del origen al Nodo
        Map<Nodo, Nodo> previo = new HashMap<>(); //Nodo por el que se pasa antes de llegar a este 
        // Cola de prioridades que ordena en base a la distancia más pequeña (en orden natural); nos referimos a distancia total desde
        // origen a Nodo.
        PriorityQueue<Map.Entry<Nodo, Double>> cola = 
                new PriorityQueue<>(Map.Entry.comparingByValue());

        distancia.put(origen, 0.0); //Agregamos que la distancia al origen es 0
        cola.add(Map.entry(origen, 0.0)); //Agregamos a la cola este nodo

        while (!cola.isEmpty()) {
            Map.Entry<Nodo, Double> entrada = cola.poll(); //Obtiene el nodo con el peso más pequeño
            Nodo actual = entrada.getKey(); 

            //Busca si ya se conoce una distancia a este nodo; en caso de que esta sea peor lo ignora.
            if (entrada.getValue() > distancia.getOrDefault(actual, Double.POSITIVE_INFINITY)) { 
                continue;
            }
            // La cola de prioridad se asegura de que actual tenga la distancia más pequeña, todas las demás opciones ya tiene de 
            // base una mayor distancia que esta, como no hay distancias 0 o negativas; este seria la solucion definitiva.
            if (actual.igual(destino)) { 
                break;
            }

            // Revisa cada vecino del nodo
            for (Vecinos v : this.getVecinosDe(actual)) {
                Nodo siguiente = v.getOtro(actual); //Cruza el camino
                double nuevaDist = distancia.get(actual) + v.getPeso(); //obtiene la distancia para llegar a ese nodo
                if (nuevaDist < distancia.getOrDefault(siguiente, Double.POSITIVE_INFINITY)) { //Actualiza los datos si es menor
                    distancia.put(siguiente, nuevaDist);
                    previo.put(siguiente, actual);
                    cola.add(Map.entry(siguiente, nuevaDist));
                }
            }
        }

        if (!distancia.containsKey(destino)) {
            return null;
        }

    
        ArrayList<Nodo> camino = new ArrayList<>();
        for (Nodo n = destino; n != null; n = previo.get(n)) {
            camino.addFirst(n);
        }
        return new Ruta(camino); 
    }

    public Nodo getNodo(int[] idNodo) {
        Nodo resultado = null;
        if (this.zonas.size() > idNodo[0]) {
            Zona zonaTemp = this.zonas.get(idNodo[0]);
            if (zonaTemp.getNodos().size() > idNodo[1]) {
                resultado = zonaTemp.getNodos().get(idNodo[1]);
            }
        }
        return resultado;
    }

    public HashMap<Nodo, ArrayList<Vecinos>> getVecinos() {
        return this.vecinos;
    }

    public ArrayList<Vecinos> getVecinosDe(Nodo nodo) {
        ArrayList<Vecinos> lista = this.vecinos.get(nodo);
        return lista;
    }
    public double getPeso(Nodo a, Nodo b) {
        for (Vecinos v : this.getVecinosDe(a)) {
            if (v.formaParte(b)) {
                return v.getPeso();
            }
        }
        return Double.POSITIVE_INFINITY; //Valor no valido
    }
}
