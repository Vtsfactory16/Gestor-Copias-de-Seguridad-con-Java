package org.example;

import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class FTPManager {
    private static FTPClient clienteFTP;
    private static final String SERVIDOR = "localhost";
    private static final int PUERTO = 21;
    private static final String USUARIO = "Admin";
    private static final String PASSWORD = "Admin1.";
    Scanner scanner = new Scanner(System.in);

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

    private static void subirArchivo(String nombreZip) {
        clienteFTP = new FTPClient();
        try (InputStream is = new FileInputStream(nombreZip) {
        }) {
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
