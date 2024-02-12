package org.example;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Pedir la ruta de la carpeta o archivo a copiar por pantalla
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce la ruta de la carpeta o archivo a copiar:");
        String rutaACopiar = scanner.nextLine();

        // Carpeta local y manager FTP
        File carpetaLocal = new File(rutaACopiar);
        FTPManager ftpManager = new FTPManager();

        try {
            // Conectar al servidor FTP
            ftpManager.conectar();

            // Realizar copia de seguridad completa
            BackupManager backupManager = new BackupManager(carpetaLocal, ftpManager);
            backupManager.realizarCopiaCompleta();


            // Desconectar del servidor FTP
            ftpManager.desconectar();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
