package org.example;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class FTPManagerTest {

    @Test
    void comprimirCarpeta_ValidPath_ReturnsZipFileName() throws IOException {
        // Arrange
        String rutaCarpeta = "D:\\FTP";

        // Act
        String nombreZip = FTPManager_EjercicioA.comprimirCarpeta(rutaCarpeta);

        // Assert
        assertNotNull(nombreZip);
        assertTrue(nombreZip.endsWith(".zip"));
    }

    @Test
    void subirArchivo_ValidFileName_UploadsSuccessfully() {
        // Arrange
        String nombreZip = "D:\\FTP\\Nuevo Archivo WinRAR ZIP.zip";

        // Act
        assertDoesNotThrow(() -> FTPManager_EjercicioA.subirArchivo(nombreZip));


    }



}
