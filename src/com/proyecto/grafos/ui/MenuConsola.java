package com.proyecto.grafos.ui;

import com.proyecto.grafos.model.Grafo;
import com.proyecto.grafos.model.Trayectoria;
import com.proyecto.grafos.service.LectorGrafo;
import com.proyecto.grafos.service.DijkstraService;
import com.proyecto.grafos.service.FloydService;
import java.io.File;

import java.util.List;
import java.util.Scanner;

public class MenuConsola {
    private Grafo grafoActual;
    private Scanner scanner;
    private LectorGrafo lector;
    private DijkstraService dijkstraService;
    private FloydService floydService; 

    public MenuConsola() {
        this.scanner = new Scanner(System.in);
        this.lector = new LectorGrafo();
        this.dijkstraService = new DijkstraService();
        this.floydService = new FloydService();
        this.grafoActual = null;
    }

    public void iniciar() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\nSISTEMA DE RUTA CORTA");
            System.out.println("El programa evalua la ruta más corta entre dos nodos utilizando los algoritmos de Dijkstra y Floyd.");
            System.out.println("1. Cargar grafo desde archivo (.txt/.csv)");
            System.out.println("2. Consultar trayectoria Dijkstra");
            System.out.println("3. Consultar trayectoria Floyd");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    cargarGrafo();
                    break;
                case "2":
                    ejecutarAlgoritmo("Dijkstra");
                    break;
                case "3":
                    ejecutarAlgoritmo("Floyd");
                    break;
                case "4":
                    salir = true;
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    private void cargarGrafo() {
        File carpeta = new File("grafos");
        
        // Verificamos si la carpeta "grafos" existe en la raíz del proyecto
        if (carpeta.exists() && carpeta.isDirectory()) {
            // Filtramos para mostrar solo archivos .txt o .csv
            File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".txt") || name.endsWith(".csv"));
            
            if (archivos != null && archivos.length > 0) {
                System.out.println("Archivos detectados en la carpeta 'grafos/':");
                for (int i = 0; i < archivos.length; i++) {
                    System.out.println("   [" + (i + 1) + "] " + archivos[i].getName());
                }
                System.out.println("   [0] Ingresar una ruta manualmente");
                System.out.print("Elige un archivo (0-" + archivos.length + "): ");
                
                try {
                    int seleccion = Integer.parseInt(scanner.nextLine());
                    if (seleccion > 0 && seleccion <= archivos.length) {
                        // El usuario eligió un archivo de la lista
                        String rutaElegida = archivos[seleccion - 1].getPath();
                        procesarCarga(rutaElegida);
                        return; // Salimos del método tras cargar con éxito
                    } else if (seleccion != 0) {
                        System.out.println("Selección inválida. Cambiando a modo manual.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Cambiando a modo manual.");
                }
            }
        }

        // Si la carpeta no existe, está vacía, o el usuario eligió [0]
        System.out.print("\nIngresa la ruta completa del archivo (ej. grafos/mi_grafo.txt): ");
        String ruta = scanner.nextLine();
        procesarCarga(ruta);
    }

    // Método auxiliar para no repetir código de carga
    private void procesarCarga(String ruta) {
        Grafo grafoTemporal = lector.cargarDesdeArchivo(ruta);
        if (grafoTemporal != null) {
            this.grafoActual = grafoTemporal;
        }
    }

    private void ejecutarAlgoritmo(String tipoAlgoritmo) {
        if (this.grafoActual == null) {
            System.out.println("Error: Primero debes cargar un grafo (Opción 1).");
            return;
        }

        try {
            System.out.print("Ingresa el Nodo Origen: ");
            int origen = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Ingresa el Nodo Destino: ");
            int destino = Integer.parseInt(scanner.nextLine());

            // Validar límites
            if (origen < 0 || origen >= grafoActual.getNumNodos() || destino < 0 || destino >= grafoActual.getNumNodos()) {
                System.out.println("Error: Los nodos deben estar entre 0 y " + (grafoActual.getNumNodos() - 1));
                return;
            }

            if (tipoAlgoritmo.equals("Dijkstra")) {
                System.out.println("\nCalculando con algoritmo de Dijkstra");
                Trayectoria resultado = dijkstraService.calcular(grafoActual, origen, destino);
                imprimirResultado(resultado, origen, destino);

            } else if (tipoAlgoritmo.equals("Floyd")) {
                System.out.println("\nCalculando con algoritmo de Floyd");
                Trayectoria resultado = floydService.calcular(grafoActual, origen, destino);
                imprimirResultado(resultado, origen, destino);
            }
        }
        catch (NumberFormatException e) {
            System.out.println("error: se debe ingresar números enteros válidos para los nodos");
        }
    }

    // Método centralizado para imprimir la UI cumpliendo los requerimientos exactos
    private void imprimirResultado(Trayectoria t, int origen, int destino) {
        System.out.println("--------------------------------------------------");
        if (!t.isAlcanzable()) {
            System.out.println("RESULTADO: El nodo destino (" + destino + ") es INALCANZABLE desde el origen (" + origen + ").");
        } else {
            System.out.println("Ruta encontrada");
            System.out.println("Costo total de la trayectoria: " + t.getCostoTotal());
            
            System.out.print("Secuencia del grafo: ");
            List<Integer> ruta = t.getRutaNodos();
            
            for (int i = 0; i < ruta.size(); i++) {
                System.out.print(ruta.get(i));
                if (i < ruta.size() - 1) {
                    System.out.print(" -> "); // Formato visual de flechas requerido
                }
            }
            System.out.println(); // Salto de línea final
        }
        System.out.println("--------------------------------------------------");
    }
}