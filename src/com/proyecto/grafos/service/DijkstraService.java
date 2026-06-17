package com.proyecto.grafos.service;

import com.proyecto.grafos.model.Grafo;
import com.proyecto.grafos.model.Trayectoria;
import java.util.Collections;

public class DijkstraService {

    public Trayectoria calcular(Grafo grafo, int origen, int destino) {
        int n = grafo.getNumNodos();
        
        // 1. Inicializar los arreglos requeridos por el proyecto
        int[] distancias = new int[n];
        boolean[] visitados = new boolean[n];
        int[] predecesores = new int[n];
        
        // Estado inicial de los arreglos
        for (int i = 0; i < n; i++) {
            distancias[i] = Grafo.INFINITO;
            visitados[i] = false;
            predecesores[i] = -1; // -1 indica que aún no tiene un nodo "padre"
        }
        
        // La distancia del origen a sí mismo es 0
        distancias[origen] = 0;
        
        // 2. Ciclo principal del algoritmo O(V^2)
        for (int i = 0; i < n - 1; i++) {
            // Obtener el nodo no visitado con la distancia mínima acumulada
            int u = obtenerNodoDistanciaMinima(distancias, visitados, n);
            
            // Si el nodo mínimo no existe, es inalcanzable, o si ya llegamos al destino, detenemos el proceso
            if (u == -1 || distancias[u] == Grafo.INFINITO || u == destino) {
                break; 
            }
            
            // Marcar el nodo como parte del conjunto permanente
            visitados[u] = true;
            
            // 3. Proceso de relajación (Actualizar las distancias de los vecinos)
            for (int v = 0; v < n; v++) {
                int pesoArista = grafo.getPeso(u, v);
                
                if (!visitados[v] && pesoArista != Grafo.INFINITO && 
                    distancias[u] + pesoArista < distancias[v]) {
                    
                    distancias[v] = distancias[u] + pesoArista;
                    predecesores[v] = u; // Guardamos el rastro para reconstruir la ruta
                }
            }
        }
        
        // 4. Empaquetar el resultado
        return construirTrayectoria(destino, distancias, predecesores);
    }

    //Métodos Auxiliares

    private int obtenerNodoDistanciaMinima(int[] distancias, boolean[] visitados, int n) {
        int min = Grafo.INFINITO;
        int minIndex = -1;
        
        for (int v = 0; v < n; v++) {
            if (!visitados[v] && distancias[v] <= min) {
                min = distancias[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    private Trayectoria construirTrayectoria(int destino, int[] distancias, int[] predecesores) {
        // Si el destino mantiene su valor infinito, no hay camino posible
        if (distancias[destino] == Grafo.INFINITO) {
            return new Trayectoria(false);
        }
        
        Trayectoria trayectoria = new Trayectoria(true);
        trayectoria.setCostoTotal(distancias[destino]);
        
        // Reconstruimos la ruta navegando por los predecesores (desde el destino hasta el origen)
        int actual = destino;
        while (actual != -1) {
            trayectoria.agregarNodoRuta(actual);
            actual = predecesores[actual];
        }
        
        // Como la ruta se extrajo de atrás hacia adelante, la invertimos
        Collections.reverse(trayectoria.getRutaNodos());
        
        return trayectoria;
    }
}