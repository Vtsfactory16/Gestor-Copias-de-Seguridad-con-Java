package org.example;

import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Clase para gestionar la transferencia de archivos a través de FTP.
 */
public class FTPManager_EjercicioA {
    private static FTPClient clienteFTP;
    private static final String SERVIDOR = "localhost"; // Dirección del servidor FTP
    private static final int PUERTO = 21; // Puerto por defecto para FTP
    private static final String USUARIO = "Admin"; // Nombre de usuario para acceder al servidor FTP
    private static final String PASSWORD = "Admin1."; // Contraseña para acceder al servidor FTP

    /**
     * Método principal para la ejecución del programa.
     * Permite al usuario introducir la ruta de la carpeta o archivo a copiar,
     * comprime la carpeta y sube el archivo comprimido al servidor FTP.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce la ruta de la carpeta o archivo a copiar:");
        String rutaACopiar = scanner.nextLine();

        try {
            String nombreZip = comprimirCarpeta(rutaACopiar);
            subirArchivo(nombreZip);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Comprime una carpeta en formato zip.
     * Utiliza el programa 7z para realizar la compresión.
     *
     * @param rutaCarpeta Ruta de la carpeta a comprimir.
     * @return El nombre del archivo zip creado.
     * @throws IOException Si ocurre algún error durante la compresión.
     */
    public static String comprimirCarpeta(String rutaCarpeta) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String nombreZip = sdf.format(new Date()) + ".zip";
        ProcessBuilder processBuilder = new ProcessBuilder("7z", "a", nombreZip, rutaCarpeta);
        Process process = processBuilder.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return nombreZip;
    }

    /**
     * Sube un archivo al servidor FTP.
     *
     * @param nombreZip Nombre del archivo a subir.
     */
    public static void subirArchivo(String nombreZip) {
        clienteFTP = new FTPClient();
        try (InputStream is = new FileInputStream(nombreZip)) {
            clienteFTP.connect(SERVIDOR, PUERTO);
            clienteFTP.login(USUARIO, PASSWORD);
            clienteFTP.setFileType(FTPClient.BINARY_FILE_TYPE);
            clienteFTP.enterLocalPassiveMode();
            clienteFTP.storeFile(nombreZip, is);

            clienteFTP.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clienteFTP.isConnected()) {
                try {
                    clienteFTP.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
