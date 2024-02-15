package org.example;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para sincronizar archivos entre una carpeta local y un servidor FTP.
 */
public class SincronizadorFTP_EjercicioB {

    private static final String SERVIDOR_FTP = "localhost"; // Dirección del servidor FTP
    private static final String USUARIO = "Admin"; // Nombre de usuario para acceder al servidor FTP
    private static final String CONTRASENA = "Admin1."; // Contraseña para acceder al servidor FTP
    private static final String CARPETA_LOCAL = "D:\\FTP"; // Carpeta local para sincronizar
    private static final String CARPETA_REMOTA = "/"; // Carpeta remota en el servidor FTP
    private static final long TIEMPO_REFRESCO = 30000; // Tiempo de refresco en milisegundos (30 segundos)

    /**
     * Método principal para la ejecución del programa.
     * Sincroniza los archivos entre la carpeta local y el servidor FTP de forma continua.
     *
     * @param args Argumentos de línea de comandos (no se utilizan).
     * @throws IOException Si ocurre algún error de E/S durante la sincronización.
     */
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

    /**
     * Sincroniza los archivos entre la carpeta local y el servidor FTP.
     *
     * @throws IOException Si ocurre algún error de E/S durante la sincronización.
     */
    private static void sincronizar() throws IOException {
        FTPClient clienteFTP = new FTPClient();
        clienteFTP.connect(SERVIDOR_FTP);
        clienteFTP.login(USUARIO, CONTRASENA);

        List<String> archivosRemotos = obtenerArchivosRemotos(clienteFTP, CARPETA_REMOTA);
        List<String> carpetasRemotas = obtenerCarpetasRemotas(clienteFTP, CARPETA_REMOTA);

        List<String> archivosLocales = obtenerArchivosLocales(CARPETA_LOCAL);
        List<String> carpetasLocales = obtenerCarpetasLocales(CARPETA_LOCAL);

        for (String archivoRemoto : archivosRemotos) {
            if (!archivosLocales.contains(archivoRemoto)) {
                borrarArchivo(clienteFTP, archivoRemoto);
            }
        }

        for (String carpetaRemota : carpetasRemotas) {
            if (!carpetasLocales.contains(carpetaRemota)) {
                borrarCarpeta(clienteFTP, carpetaRemota);
            }
        }

        for (String archivoLocal : archivosLocales) {
            if (!archivosRemotos.contains(archivoLocal) || esArchivoActualizado(clienteFTP, CARPETA_REMOTA, archivoLocal, new File(CARPETA_LOCAL + File.separator + archivoLocal).lastModified())) {
                addArchivo(clienteFTP, archivoLocal);
            }
        }

        for (String carpetaLocal : carpetasLocales) {
            if (!carpetasRemotas.contains(carpetaLocal)) {
                addCarpeta(clienteFTP, carpetaLocal);
            }
        }

        clienteFTP.disconnect();
    }

    /**
     * Obtiene la lista de archivos remotos en la carpeta especificada en el servidor FTP.
     *
     * @param clienteFTP Cliente FTP conectado al servidor.
     * @param carpeta    Carpeta remota en el servidor FTP.
     * @return Lista de nombres de archivos remotos.
     * @throws IOException Si ocurre algún error de E/S durante la obtención de archivos remotos.
     */
    static List<String> obtenerArchivosRemotos(FTPClient clienteFTP, String carpeta) throws IOException {
        List<String> archivos = new ArrayList<>();
        clienteFTP.changeWorkingDirectory(carpeta);
        for (FTPFile archivo : clienteFTP.listFiles()) {
            if (archivo.isFile()) {
                archivos.add(archivo.getName());
            }
        }
        return archivos;
    }

    /**
     * Obtiene la lista de carpetas remotas en la carpeta especificada en el servidor FTP.
     *
     * @param clienteFTP Cliente FTP conectado al servidor.
     * @param carpeta    Carpeta remota en el servidor FTP.
     * @return Lista de nombres de carpetas remotas.
     * @throws IOException Si ocurre algún error de E/S durante la obtención de carpetas remotas.
     */
    static List<String> obtenerCarpetasRemotas(FTPClient clienteFTP, String carpeta) throws IOException {
        List<String> carpetas = new ArrayList<>();
        clienteFTP.changeWorkingDirectory(carpeta);
        for (FTPFile archivo : clienteFTP.listFiles()) {
            if (archivo.isDirectory()) {
                carpetas.add(archivo.getName());
            }
        }
        return carpetas;
    }

    /**
     * Obtiene la lista de archivos locales en la carpeta especificada.
     *
     * @param carpeta Carpeta local.
     * @return Lista de nombres de archivos locales.
     */
    static List<String> obtenerArchivosLocales(String carpeta) {
        List<String> archivos = new ArrayList<>();
        File directorio = new File(carpeta);
        File[] listaArchivos = directorio.listFiles();
        if (listaArchivos != null) {
            for (File archivo : listaArchivos) {
                if (archivo.isFile()) {
                    archivos.add(archivo.getName());
                }
            }
        }
        return archivos;
    }

    /**
     * Obtiene la lista de carpetas locales en la carpeta especificada.
     *
     * @param carpeta Carpeta local.
     * @return Lista de nombres de carpetas locales.
     */
    static List<String> obtenerCarpetasLocales(String carpeta) {
        List<String> carpetas = new ArrayList<>();
        File directorio = new File(carpeta);
        File[] listaArchivos = directorio.listFiles();
        if (listaArchivos != null) {
            for (File archivo : listaArchivos) {
                if (archivo.isDirectory()) {
                    carpetas.add(archivo.getName());
                }
            }
        }
        return carpetas;
    }

    /**
     * Borra un archivo remoto del servidor FTP. Y la borra con el método deleteFile.
     *
     * @param clienteFTP Cliente FTP conectado al servidor.
     * @param archivo    Nombre del archivo remoto a borrar.
     * @throws IOException Si ocurre algún error de E/S durante el borrado del archivo.
     */
    static void borrarArchivo(FTPClient clienteFTP, String archivo) throws IOException {
        clienteFTP.deleteFile(archivo);
    }

    /**
     * Borra una carpeta remota del servidor FTP. Y la borra con el método removeDirectory.
     *
     * @param clienteFTP Cliente FTP conectado al servidor.
     * @param carpeta    Nombre de la carpeta remota a borrar.
     * @throws IOException Si ocurre algún error de E/S durante el borrado de la carpeta.
     */
    static void borrarCarpeta(FTPClient clienteFTP, String carpeta) throws IOException {
        clienteFTP.removeDirectory(carpeta);
    }

    /**
     * Agrega un archivo al servidor FTP. Utilizando fileInputStream para leer el archivo local
     * y almacenarlo en el servidor FTP.
     *
     * @param clienteFTP Cliente FTP conectado al servidor.
     * @param archivo    Nombre del archivo local a agregar.
     * @throws IOException Si ocurre algún error de E/S durante la transferencia del archivo.
     */
    static void addArchivo(FTPClient clienteFTP, String archivo) throws IOException {
        File localFile = new File(CARPETA_LOCAL + File.separator + archivo);
        FileInputStream fis = new FileInputStream(localFile);
        clienteFTP.storeFile(archivo, fis);
        fis.close();
    }

    /**
     * Crea una carpeta en el servidor FTP. Y la crea con el método makeDirectory.
     *
     * @param clienteFTP Cliente FTP conectado al servidor.
     * @param carpeta    Nombre de la carpeta a crear.
     * @throws IOException Si ocurre algún error de E/S durante la creación de la carpeta.
     */
    static void addCarpeta(FTPClient clienteFTP, String carpeta) throws IOException {
        clienteFTP.makeDirectory(carpeta);
    }

    /**
     * Verifica si un archivo local ha sido actualizado desde su última modificación en el servidor FTP.
     *
     * @param clienteFTP            Cliente FTP conectado al servidor.
     * @param carpetaRemota         Carpeta remota en el servidor FTP.
     * @param nombreArchivo         Nombre del archivo local.
     * @param ultimaModificacionLocal    Fecha de la última modificación del archivo local (en milisegundos).
     * @return true si el archivo local ha sido actualizado; false en caso contrario.
     * @throws IOException Si ocurre algún error de E/S durante la verificación.
     */
    static boolean esArchivoActualizado(FTPClient clienteFTP, String carpetaRemota, String nombreArchivo, long ultimaModificacionLocal) throws IOException {
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
