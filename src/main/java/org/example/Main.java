package org.example;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Carpeta local y manager FTP
        File carpetaLocal = new File("D:\\FTP\\");
        FTPManager ftpManager = new FTPManager();

        try {
            // Conectar al servidor FTP
            ftpManager.conectar();

            // Realizar copia de seguridad completa
            BackupManager backupManager = new BackupManager(carpetaLocal, ftpManager);
            backupManager.realizarCopiaCompleta();

            // Comparar y actualizar carpetas
            ComparadorCarpetas comparadorCarpetas = new ComparadorCarpetas(carpetaLocal, ftpManager);
            comparadorCarpetas.compararYActualizar();

            // Descomprimir archivo descargado
            String archivoZipDescargado = "descomprimiddo.zip";
            String directorioSalidaDescompresion = "D:\\FTP\\descomprimido";
            ftpManager.descomprimirZip("D:\\FTP\\" + archivoZipDescargado, directorioSalidaDescompresion);

            // Desconectar del servidor FTP
            ftpManager.desconectar();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
