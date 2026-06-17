package com.proyecto.grafos.model;

public class Grafo {
    // La estructura central protegida de manipulaciones externas
    private int[][] matrizAdyacencia;
    private int numNodos;
    
    // Usamos un número muy grande pero seguro para evitar desbordamientos (overflow)
    public static final int INFINITO = 99999999; 

    public Grafo(int numNodos) {
        this.numNodos = numNodos;
        this.matrizAdyacencia = new int[numNodos][numNodos];
        
        // Inicializamos la matriz
        for (int i = 0; i < numNodos; i++) {
            for (int j = 0; j < numNodos; j++) {
                if (i == j) {
                    // La distancia de un nodo a sí mismo siempre es 0
                    matrizAdyacencia[i][j] = 0; 
                } else {
                    // Si no son el mismo nodo, asumimos que no hay conexión inicial
                    matrizAdyacencia[i][j] = INFINITO;
                }
            }
        }
    }

    public void agregarArista(int origen, int destino, int peso) {
        matrizAdyacencia[origen][destino] = peso;
        
        // matrizAdyacencia[destino][origen] = peso;
    }

    public int getPeso(int origen, int destino) {
        return matrizAdyacencia[origen][destino];
    }

    public int getNumNodos() {
        return numNodos;
    }
}