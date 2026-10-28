package src.mapa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import src.utilidades.Constantes;
import src.utilidades.Punto;

public class Mapa {
    private static volatile Mapa instance = null;
    private ArrayList<Zona> zonas;
    private HashMap<Nodo, ArrayList<Vecinos>> vecinos;

    private Mapa() {
        this.zonas = new ArrayList<>();
        this.vecinos = new HashMap<>();
        crearZonas();
        crearNodos();
        crearVecinos();
    }


    private void crearNodos() {
    for (int idZona = 0; idZona < Constantes.TOTAL_ZONES; idZona++) {
        Zona zona = zonas.get(idZona);
        double[][] coords = Constantes.COORDENADAS_NODOS[idZona];
        for (int idNodo = 0; idNodo < coords.length; idNodo++) {
            Punto pos = new Punto(coords[idNodo][0], coords[idNodo][1]);
            // La zona se pasa en el constructor porque hashCode() la necesita
            Nodo nodo = new Nodo(idNodo, pos, zona);
            zona.agregarNodo(nodo);
            vecinos.put(nodo, new ArrayList<>());
        }
    }
}

    private void crearVecinos() {
        for (int[] c : Constantes.CONEXIONES) {
            Nodo a = getNodo(new int[]{c[0], c[1]});
            Nodo b = getNodo(new int[]{c[2], c[3]});
            double peso = a.getPosicion().distanciaCon(b.getPosicion());

            // Un solo objeto Vecinos compartido por A y B
            Vecinos v = new Vecinos(a, b, peso);
            vecinos.get(a).add(v);
            vecinos.get(b).add(v);
        }
    }
    private void crearZonas() {
        for (int idZona = 0; idZona < Constantes.TOTAL_ZONES; idZona++) {
            zonas.add(new Zona(idZona, this));
        }
    }

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
        if (origen == null || destino == null
                || !vecinos.containsKey(origen)
                || !vecinos.containsKey(destino)) {
            return null;
        }

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
        if (idNodo == null || idNodo.length < 2
                || idNodo[0] < 0 || idNodo[1] < 0
                || idNodo[0] >= zonas.size()) {
            return null;
        }
        Zona zona = zonas.get(idNodo[0]);
        if (idNodo[1] >= zona.getNodos().size()) {
            return null;
        }
        return zona.getNodos().get(idNodo[1]);
    }

    public HashMap<Nodo, ArrayList<Vecinos>> getVecinos() {
        return this.vecinos;
    }

    public ArrayList<Vecinos> getVecinosDe(Nodo nodo) {
        ArrayList<Vecinos> lista = this.vecinos.get(nodo);
        return lista == null ? new ArrayList<>() : new ArrayList<>(lista);
    }

    public double getPeso(Nodo a, Nodo b) {
        for (Vecinos v : getVecinosDe(a)) {
            if (v.formaParte(b)) {
                return v.getPeso();
            }
        }
        return Double.POSITIVE_INFINITY; //Valor no valido
    }
}
