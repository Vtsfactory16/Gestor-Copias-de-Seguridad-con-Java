package org.example;

import java.io.File;
import java.io.IOException;

public class ComparadorCarpetas {
    private File carpetaLocal;
    private FTPManager ftpManager;

    public ComparadorCarpetas(File carpetaLocal, FTPManager ftpManager) {
        this.carpetaLocal = carpetaLocal;
        this.ftpManager = ftpManager;
    }

    public void compararYActualizar() {
        try {
            String[] archivosRemotos = ftpManager.listarArchivosRemotos();
            for (String archivoRemoto : archivosRemotos) {
                // Comprobar si el archivo remoto está presente en la carpeta local
                File archivoLocal = new File(carpetaLocal, archivoRemoto);
                if (!archivoLocal.exists()) {
                    // Si el archivo no existe localmente, descargarlo
                    ftpManager.descargarArchivo(archivoRemoto, archivoLocal.getAbsolutePath());
                    System.out.println("Archivo descargado correctamente: " + archivoRemoto);
                }
            }
            System.out.println("Comparación y actualización de carpetas finalizada.");

            // Descomprimir archivos descargados
            for (String archivoRemoto : archivosRemotos) {
                File archivoLocal = new File(carpetaLocal, archivoRemoto);
                if (archivoLocal.getName().endsWith(".zip")) {
                    String directorioSalidaDescompresion = carpetaLocal.getAbsolutePath();
                    ftpManager.descomprimirZip(archivoLocal.getAbsolutePath(), directorioSalidaDescompresion);
                    System.out.println("Archivo ZIP descomprimido correctamente: " + archivoLocal.getName());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al comparar y actualizar carpetas: " + e.getMessage());
        }
    }
}
