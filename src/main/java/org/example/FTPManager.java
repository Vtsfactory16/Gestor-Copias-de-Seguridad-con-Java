package org.example;
import java.io.*;
import java.net.SocketException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPManager {
    private final FTPClient clienteFTP;
    private static final String SERVIDOR = "localhost";
    private static final int PUERTO = 21;
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";

    public FTPManager() {
        clienteFTP = new FTPClient();
    }

    public void conectar() throws SocketException, IOException {
        clienteFTP.connect(SERVIDOR, PUERTO);
        int respuesta = clienteFTP.getReplyCode();

        if (!FTPReply.isPositiveCompletion(respuesta)) {
            clienteFTP.disconnect();
            throw new IOException("Error al conectar con el servidor FTP");
        }

        boolean credencialesOK = clienteFTP.login(USUARIO, PASSWORD);

        if (!credencialesOK) {
            throw new IOException("Error al conectar con el servidor FTP. Credenciales incorrectas.");
        }

        clienteFTP.setFileType(FTP.BINARY_FILE_TYPE);
    }

    public void desconectar() throws IOException {
        clienteFTP.disconnect();
    }

    public boolean subirArchivo(String pathLocal) throws IOException {
        File ficheroLocal = new File(pathLocal);
        String nombreFichero = ficheroLocal.getName();
        InputStream is = new FileInputStream(ficheroLocal);
        boolean subido = clienteFTP.storeFile("D:\\FTP\\" + nombreFichero, is); // Concatenamos la ruta remota deseada
        is.close();
        return subido;
    }


    public boolean descargarArchivo(String ficheroRemoto, String pathLocal) throws IOException {
        OutputStream os = new FileOutputStream(pathLocal);
        boolean recibido = clienteFTP.retrieveFile(ficheroRemoto, os);
        os.close();
        return recibido;
    }

    public void comprimirCarpeta(String pathCarpeta, String pathZip) throws IOException {
        File carpeta = new File(pathCarpeta);
        FileOutputStream fos = new FileOutputStream(pathZip);
        ZipOutputStream zos = new ZipOutputStream(fos);
        comprimir(carpeta, carpeta.getName(), zos);
        zos.close();
    }

    private void comprimir(File archivo, String nombreArchivo, ZipOutputStream zos) throws IOException {
        if (archivo.isDirectory()) {
            if (nombreArchivo.endsWith("/")) {
                zos.putNextEntry(new ZipEntry(nombreArchivo));
                zos.closeEntry();
            } else {
                zos.putNextEntry(new ZipEntry(nombreArchivo + "/"));
                zos.closeEntry();
            }
            File[] archivos = archivo.listFiles();
            for (File archivoHijo : archivos) {
                comprimir(archivoHijo, nombreArchivo + "/" + archivoHijo.getName(), zos);
            }
            return;
        }

        FileInputStream fis = new FileInputStream(archivo);
        ZipEntry zipEntry = new ZipEntry(nombreArchivo);
        zos.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        fis.close();
    }

    public void descomprimirZip(String pathZip, String directorioSalida) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(pathZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            String nombreArchivo = zipEntry.getName();
            File nuevoArchivo = new File(directorioSalida + File.separator + nombreArchivo);
            nuevoArchivo.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(nuevoArchivo);
            int longitud;
            while ((longitud = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, longitud);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }
}
