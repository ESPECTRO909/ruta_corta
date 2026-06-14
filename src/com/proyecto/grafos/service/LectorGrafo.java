package com.proyecto.grafos.service;

import com.proyecto.grafos.model.Grafo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LectorGrafo {

    public Grafo cargarDesdeArchivo(String rutaArchivo) {
        Grafo grafo = null;

        // Usamos try-with-resources para asegurar que el archivo se cierre automáticamente
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            
            // 1. Leer la primera línea para obtener el número total de nodos
            String primeraLinea = br.readLine();
            if (primeraLinea == null || primeraLinea.trim().isEmpty()) {
                throw new IllegalArgumentException("El archivo está vacío");
            }
            
            int numNodos = Integer.parseInt(primeraLinea.trim());
            grafo = new Grafo(numNodos);

            // 2. Leer el resto de las líneas (las aristas/conexiones)
            String linea;
            int numLinea = 2; // Llevamos el conteo para dar mensajes de error precisos
            
            while ((linea = br.readLine()) != null) {
                // Ignorar líneas en blanco
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(",");
                
                // Validar que la línea tenga exactamente 3 datos: Origen, Destino, Peso
                if (partes.length == 3) {
                    int origen = Integer.parseInt(partes[0].trim());
                    int destino = Integer.parseInt(partes[1].trim());
                    int peso = Integer.parseInt(partes[2].trim());
                    
                    // Validar que los nodos no excedan el tamaño de la matriz
                    if (origen >= numNodos || destino >= numNodos || origen < 0 || destino < 0) {
                        System.out.println("Advertencia en línea " + numLinea + ": Nodo fuera de rango. Se ignorará esta arista.");
                    } else {
                        grafo.agregarArista(origen, destino, peso);
                    }
                } else {
                    System.out.println("Advertencia en la línea " + numLinea + ": Formato incorrecto. Se va ignorar esta línea.");
                }
                numLinea++;
            }
            System.out.println("Grafo cargado exitosamente en memoria con " + numNodos + " nodos.");

        } catch (IOException e) {
            System.err.println("error al leer el archivo: Verifica que la ruta '" + rutaArchivo + "' sea correcta y el archivo exista.");
        } catch (NumberFormatException e) {
            System.err.println("Error de formato: Se encontraron letras o caracteres inválidos donde se esperaban números.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error de estructura: " + e.getMessage());
        }

        return grafo; // Si ocurre un error grave, devolverá null, y nuestro menú sabrá manejarlo.
    }
}