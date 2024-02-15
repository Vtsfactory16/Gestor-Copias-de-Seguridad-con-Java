package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PruebaEstres {
    public static void crearArchivosTxt(String carpeta, int cantidad) {
        File directorio = new File(carpeta);
        // Verificar si la carpeta existe, si no, crearla
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("Carpeta creada: " + carpeta);
            } else {
                System.out.println("No se pudo crear la carpeta: " + carpeta);
                return;
            }
        }
        // Crear los archivos de texto
        for (int i = 1; i <= cantidad; i++) {
            String nombreArchivo = "archivo" + i + ".txt";
            String contenido = "Contenido del archivo " + i;
            File archivo = new File(directorio, nombreArchivo);
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(contenido);
                System.out.println("Archivo creado: " + archivo.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Error al crear el archivo: " + e.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        String carpeta = "D:\\FTP";
        int cantidadArchivos = 1000;

        crearArchivosTxt(carpeta, cantidadArchivos);
    }
}
