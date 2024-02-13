package org.example;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SincronizadorFTP {

    private static final String SERVIDOR_FTP = "localhost";
    private static final String USUARIO = "Admin";
    private static final String CONTRASENA = "Admin1.";
    private static final String CARPETA_LOCAL = "D:\\FTP";
    private static final String CARPETA_REMOTA = "C:\\FTP";
    private static final long TIEMPO_REFRESCO = 15000; // 30 segundos

    public static void main(String[] args) throws IOException {
        while (true) {
            sincronizar();
            try {
                Thread.sleep(TIEMPO_REFRESCO);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sincronizar() throws IOException {
        FTPClient clienteFTP = new FTPClient();
        clienteFTP.connect(SERVIDOR_FTP);
        clienteFTP.login(USUARIO, CONTRASENA);

        List<String> archivosRemotos = obtenerArchivosRemotos(clienteFTP, CARPETA_REMOTA);
        List<String> archivosLocales = obtenerArchivosLocales(CARPETA_LOCAL);

      for (String archivoRemoto : archivosRemotos) {
            if (!archivosLocales.contains(archivoRemoto)) {
                borrarArchivo(clienteFTP, archivoRemoto);
            }
        }
      for (String archivoLocal : archivosLocales) {
            if (!archivosRemotos.contains(archivoLocal) || esArchivoActualizado(clienteFTP, CARPETA_REMOTA, archivoLocal, new File(CARPETA_LOCAL + File.separator + archivoLocal).lastModified())) {
                addArchivo(clienteFTP, archivoLocal);
            }
        }

        clienteFTP.disconnect();
    }

    private static List<String> obtenerArchivosRemotos(FTPClient clienteFTP, String carpeta) throws IOException {
        List<String> archivos = new ArrayList<>();
        clienteFTP.changeWorkingDirectory(carpeta);
        for (String nombreArchivo : clienteFTP.listNames()) {
            archivos.add(nombreArchivo);
        }
        return archivos;
    }

    private static List<String> obtenerArchivosLocales(String carpeta) {
        List<String> archivos = new ArrayList<>();
        File directorio = new File(carpeta);
        File[] listaArchivos = directorio.listFiles();
        if (listaArchivos != null) {
            for (File archivo : listaArchivos) {
                archivos.add(archivo.getName());
            }
        }
        return archivos;
    }

    private static void borrarArchivo(FTPClient clienteFTP, String archivo) throws IOException {
        clienteFTP.deleteFile(archivo);
    }
    private static void addArchivo(FTPClient clienteFTP, String archivo) throws IOException {
        File localFile = new File(CARPETA_LOCAL + File.separator + archivo);
        FileInputStream fis = new FileInputStream(localFile);
        clienteFTP.storeFile(archivo, fis);
        fis.close();
    }
    private static boolean esArchivoActualizado(FTPClient clienteFTP, String carpetaRemota, String nombreArchivo, long ultimaModificacionLocal) throws IOException {
        clienteFTP.changeWorkingDirectory(carpetaRemota);
        FTPFile[] archivosRemotos = clienteFTP.listFiles();

        for (FTPFile archivoRemoto : archivosRemotos) {
            if (archivoRemoto.getName().equals(nombreArchivo)) {
                long ultimaModificacionRemota = archivoRemoto.getTimestamp().getTimeInMillis();
                return ultimaModificacionLocal > ultimaModificacionRemota;
            }
        }

        return false;
    }

}