package org.example;

import java.io.File;
import java.io.IOException;

public class BackupManager {
    private File localFolder;
    private FTPManager ftpManager;
    private File lastFullBackup;

    public BackupManager(File localFolder, FTPManager ftpManager) {
        this.localFolder = localFolder;
        this.ftpManager = ftpManager;
        this.lastFullBackup = null;
    }
    public void realizarCopiaCompleta() {
        File[] archivos = localFolder.listFiles();
        for (File archivo : archivos) {
            try {
                if (archivo.isDirectory()) {
                    // Comprimir carpeta antes de subirla
                    String nombreZip = archivo.getName() + ".zip";
                    String pathZip = localFolder.getAbsolutePath() + File.separator + nombreZip;
                    ftpManager.comprimirCarpeta(archivo.getAbsolutePath(), pathZip);
                    ftpManager.subirArchivo(pathZip);
                    System.out.println("Carpeta comprimida y subida correctamente: " + archivo.getName());
                } else {
                    ftpManager.subirArchivo(archivo.getAbsolutePath());
                    System.out.println("Archivo subido correctamente: " + archivo.getName());
                }
            } catch (IOException e) {
                System.err.println("Error al subir el archivo " + archivo.getName() + ": " + e.getMessage());
            }
        }
        System.out.println("Copia de seguridad completa realizada.");
    }

}

