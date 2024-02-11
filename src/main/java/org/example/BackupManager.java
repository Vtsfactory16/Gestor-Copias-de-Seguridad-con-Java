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


    public void realizarCopiaDiferencial() {
        if (lastFullBackup == null) {
            System.out.println("No se ha realizado ninguna copia de seguridad completa.");
            return;
        }

        File[] archivos = localFolder.listFiles();
        for (File archivo : archivos) {
            if (archivo.lastModified() > lastFullBackup.lastModified()) {
                try {
                    ftpManager.subirArchivo(archivo.getAbsolutePath());
                    System.out.println("Archivo subido correctamente (diferencial): " + archivo.getName());
                } catch (IOException e) {
                    System.err.println("Error al subir el archivo " + archivo.getName() + ": " + e.getMessage());
                }
            }
        }
        System.out.println("Copia de seguridad diferencial realizada.");
    }

    public void realizarCopiaIncremental() {
        File[] archivos = localFolder.listFiles();
        for (File archivo : archivos) {
            try {
                ftpManager.subirArchivo(archivo.getAbsolutePath());
                System.out.println("Archivo subido correctamente (incremental): " + archivo.getName());
            } catch (IOException e) {
                System.err.println("Error al subir el archivo " + archivo.getName() + ": " + e.getMessage());
            }
        }
        System.out.println("Copia de seguridad incremental realizada.");
    }

    public void sincronizarCarpetas() {
        try {
            ftpManager.conectar();

            String[] archivosRemotos = ftpManager.listarArchivosRemotos();
            for (String archivoRemoto : archivosRemotos) {
                File archivoLocal = new File(localFolder, archivoRemoto);
                if (!archivoLocal.exists()) {
                    ftpManager.descargarArchivo(archivoRemoto, archivoLocal.getAbsolutePath());
                    System.out.println("Archivo descargado correctamente: " + archivoRemoto);
                }
            }
            System.out.println("Sincronización de carpetas finalizada.");
        } catch (IOException e) {
            System.err.println("Error al sincronizar carpetas: " + e.getMessage());
        } finally {
            try {
                ftpManager.desconectar();
            } catch (IOException e) {
                System.err.println("Error al desconectar del servidor FTP: " + e.getMessage());
            }
        }
    }
    public void comprimirYSubirArchivo(String pathLocal) throws IOException, InterruptedException {
        // Nombre del archivo comprimido
        String nombreArchivoZip = obtenerNombreArchivoZip(pathLocal);

        // Comando para comprimir el archivo utilizando Compact
        String comandoCompact = "compact /c /exe:lzx /s:" + pathLocal;

        // Ejecutar el comando en un proceso secundario
        Process procesoCompact = Runtime.getRuntime().exec(comandoCompact);
        procesoCompact.waitFor(); // Esperar a que el proceso termine

        // Subir el archivo comprimido mediante FTP
        ftpManager.subirArchivo(nombreArchivoZip);
    }

    private String obtenerNombreArchivoZip(String path) {
        // Obtener el nombre del archivo sin la extensión
        String nombreArchivo = new File(path).getName().replaceFirst("[.][^.]+$", "");
        // Agregar la extensión ".zip"
        return nombreArchivo + ".zip";
    }
}

