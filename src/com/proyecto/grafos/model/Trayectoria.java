package com.proyecto.grafos.model;

import java.util.ArrayList;
import java.util.List;

public class Trayectoria {
    
    private int costoTotal;
    private List<Integer> rutaNodos;
    private boolean alcanzable;

    public Trayectoria(){}

    public Trayectoria(boolean alcanzable) {
        this.alcanzable = alcanzable;
        this.rutaNodos = new ArrayList<>();
        this.costoTotal = 0; // Inicializamos en 0
    }

    // --- Getters y Setters ---

    public void setCostoTotal(int costoTotal) {
        this.costoTotal = costoTotal;
    }

    public int getCostoTotal() {
        return costoTotal;
    }

    public void agregarNodoRuta(int nodo) {
        this.rutaNodos.add(nodo);
    }

    public List<Integer> getRutaNodos() {
        return rutaNodos;
    }

    public boolean isAlcanzable() {
        return alcanzable;
    }
}

