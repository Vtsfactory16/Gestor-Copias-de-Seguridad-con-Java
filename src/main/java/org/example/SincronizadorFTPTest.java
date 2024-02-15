package org.example;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class SincronizadorFTPTest {

    @Mock
    private FTPClient ftpClient;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testObtenerArchivosRemotos() throws IOException {
        when(ftpClient.changeWorkingDirectory(anyString())).thenReturn(true);

        FTPFile archivo1 = new FTPFile();
        archivo1.setName("archivo1.txt");
        archivo1.setType(FTPFile.FILE_TYPE);
        FTPFile archivo2 = new FTPFile();
        archivo2.setName("archivo2.txt");
        archivo2.setType(FTPFile.FILE_TYPE);
        FTPFile[] archivos = {archivo1, archivo2};

        when(ftpClient.listFiles()).thenReturn(archivos);

        List<String> resultado = SincronizadorFTP_EjercicioB.obtenerArchivosRemotos(ftpClient, "/");
        List<String> esperado = new ArrayList<>();
        esperado.add("archivo1.txt");
        esperado.add("archivo2.txt");

        assertEquals(esperado, resultado);
    }

    @Test
    public void testObtenerCarpetasRemotas() throws IOException {
        when(ftpClient.changeWorkingDirectory(anyString())).thenReturn(true);

        FTPFile carpeta1 = new FTPFile();
        carpeta1.setName("carpeta1");
        carpeta1.setType(FTPFile.DIRECTORY_TYPE);
        FTPFile carpeta2 = new FTPFile();
        carpeta2.setName("carpeta2");
        carpeta2.setType(FTPFile.DIRECTORY_TYPE);
        FTPFile[] carpetas = {carpeta1, carpeta2};

        when(ftpClient.listFiles()).thenReturn(carpetas);

        List<String> resultado = SincronizadorFTP_EjercicioB.obtenerCarpetasRemotas(ftpClient, "/");
        List<String> esperado = new ArrayList<>();
        esperado.add("carpeta1");
        esperado.add("carpeta2");

        assertEquals(esperado, resultado);
    }

    @Test
    public void testObtenerArchivosLocales() {
        String carpeta = "D:\\FTP";
        List<String> resultado = SincronizadorFTP_EjercicioB.obtenerArchivosLocales(carpeta);
        List<String> esperado = new ArrayList<>();
        esperado.add("Archivo1.txt");
        esperado.add("Archivo2.txt");

        assertEquals(esperado, resultado);
    }

    @Test
    public void testObtenerCarpetasLocales() {
        String carpeta = "D:\\FTP";
        List<String> resultado = SincronizadorFTP_EjercicioB.obtenerCarpetasLocales(carpeta);
        List<String> esperado = new ArrayList<>();
        esperado.add("carpeta1");
        esperado.add("carpeta2");

        assertEquals(esperado, resultado);
    }

    @Test
    public void testBorrarArchivo() throws IOException {
        String archivo = "archivo.txt";
        SincronizadorFTP_EjercicioB.borrarArchivo(ftpClient, archivo);
        verify(ftpClient).deleteFile(archivo);
    }

    @Test
    public void testBorrarCarpeta() throws IOException {
        String carpeta = "carpeta";
        SincronizadorFTP_EjercicioB.borrarCarpeta(ftpClient, carpeta);
        verify(ftpClient).removeDirectory(carpeta);
    }

    //test addArchivo
    @Test
    public void testAddArchivo() throws IOException {
        String archivo = "archivo.txt";
        FileInputStream is = new FileInputStream(archivo);
        SincronizadorFTP_EjercicioB.addArchivo(ftpClient, archivo);
        verify(ftpClient).storeFile(archivo, is);
    }

    @Test
    public void testAddCarpeta() throws IOException {
        String carpeta = "carpeta";
        SincronizadorFTP_EjercicioB.addCarpeta(ftpClient, carpeta);
        verify(ftpClient).makeDirectory(carpeta);
    }

    @Test
    public void testEsArchivoActualizado() throws IOException {
        String carpetaRemota = "/";
        String nombreArchivo = "archivo.txt";
        long ultimaModificacionLocal = System.currentTimeMillis(); // Simular una fecha de modificación actual

        // Simular archivos remotos
        FTPFile archivoRemoto = new FTPFile();
        archivoRemoto.setName(nombreArchivo);
        archivoRemoto.setTimestamp(null); // Para simplificar, no se establece la fecha de modificación
        FTPFile[] archivosRemotos = {archivoRemoto};
        when(ftpClient.listFiles()).thenReturn(archivosRemotos);

        boolean resultado = SincronizadorFTP_EjercicioB.esArchivoActualizado(ftpClient, carpetaRemota, nombreArchivo, ultimaModificacionLocal);
        assertFalse(resultado); // El archivo remoto no tiene fecha de modificación, por lo que se asume que no está actualizado
    }


}
