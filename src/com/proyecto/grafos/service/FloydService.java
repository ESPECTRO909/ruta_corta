package com.proyecto.grafos.service;

import com.proyecto.grafos.model.Grafo;
import com.proyecto.grafos.model.Trayectoria;
import java.util.Collections;

public class FloydService {

    public Trayectoria calcular(Grafo grafo, int origen, int destino) {
        int n = grafo.getNumNodos();
        
        // 1. Crear las matrices de distancias y predecesores
        int[][] distancias = new int[n][n];
        int[][] predecesores = new int[n][n];

        // 2. Inicialización de las matrices base
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distancias[i][j] = grafo.getPeso(i, j);
                
                // Si hay una conexión directa, el predecesor de 'j' es 'i'
                if (i != j && distancias[i][j] != Grafo.INFINITO) {
                    predecesores[i][j] = i;
                } else {
                    predecesores[i][j] = -1; // -1 significa que no hay camino aún
                }
            }
        }

        // 3. Los tres ciclos anidados de Floyd-Warshall O(V^3)
        // 'k' es el nodo intermedio que estamos evaluando
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // Validamos que existan caminos reales para evitar sumar INFINITO
                    if (distancias[i][k] != Grafo.INFINITO && distancias[k][j] != Grafo.INFINITO) {
                        
                        // Si ir de i->k->j es más barato que ir de i->j directamente:
                        if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                            distancias[i][j] = distancias[i][k] + distancias[k][j];
                            // Actualizamos el predecesor
                            predecesores[i][j] = predecesores[k][j]; 
                        }
                    }
                }
            }
        }

        // 4. Empaquetar y devolver el resultado
        return construirTrayectoria(origen, destino, distancias, predecesores);
    }

    // --- Métodos Auxiliares Privados ---

    private Trayectoria construirTrayectoria(int origen, int destino, int[][] distancias, int[][] predecesores) {
        // Si después de todo el proceso sigue siendo INFINITO, es inalcanzable
        if (distancias[origen][destino] == Grafo.INFINITO) {
            return new Trayectoria(false);
        }

        Trayectoria trayectoria = new Trayectoria(true);
        trayectoria.setCostoTotal(distancias[origen][destino]);

        // Reconstruimos la ruta navegando en la matriz de predecesores hacia atrás
        int actual = destino;
        while (actual != origen) {
            trayectoria.agregarNodoRuta(actual);
            actual = predecesores[origen][actual];
        }
        trayectoria.agregarNodoRuta(origen); // Agregamos el origen al final

        // Volteamos la lista para que quede Origen -> Intermedios -> Destino
        Collections.reverse(trayectoria.getRutaNodos());

        return trayectoria;
    }
}