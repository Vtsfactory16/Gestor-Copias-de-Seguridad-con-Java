# Gestor de Copias de Seguridad con Java

## Descripción
Este proyecto implementa un gestor de copias de seguridad utilizando Java. Permite realizar copias de seguridad completas, diferenciales e incrementales, además de sincronizar carpetas locales y remotas a través de FTP.

## Funcionalidades
1. **Realizar Copia de Seguridad Completa:**
   La función `realizarCopiaCompleta()` en `BackupManager` permite subir todos los archivos de una carpeta local al servidor FTP.

2. **Realizar Copia de Seguridad Diferencial:**
   La función `realizarCopiaDiferencial()` en `BackupManager` sube al servidor FTP solo los archivos que han cambiado desde la última copia de seguridad completa.

3. **Realizar Copia de Seguridad Incremental:**
   La función `realizarCopiaIncremental()` en `BackupManager` sube al servidor FTP todos los archivos, independientemente de si han cambiado o no desde la última copia de seguridad.

4. **Sincronizar Carpetas:**
   La función `sincronizarCarpetas()` en `BackupManager` compara y sincroniza las carpetas locales y remotas a través de FTP.

## Uso
1. Configura el servidor FTP en la clase `FTPManager`.
2. Ejecuta `Main.java` para realizar las operaciones de copia de seguridad y sincronización.

## Requisitos
- Java JDK 17 o superior
- Apache Commons Net (incluido en el proyecto)

## Instalación
1. Clona el repositorio: `https://github.com/Vtsfactory16/Gestor-Copias-de-Seguridad-con-Java.git`
2. Importa el proyecto en tu IDE de preferencia.
3. Ejecuta `Main.java` para iniciar la aplicación.

## Contribución
¡Contribuciones son bienvenidas! Si deseas contribuir a este proyecto, por favor crea un pull request.

## Licencia
Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles. 🚀
