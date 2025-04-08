# Agenda de Citas

## Descripción

Este proyecto es una aplicación de agenda de citas desarrollada en Java utilizando Swing para la interfaz gráfica. Permite gestionar citas diarias organizadas por fecha, facilitando la adición, eliminación y búsqueda de citas.

## Funcionalidades Actuales

*   **Gestión de Páginas:**
    *   La agenda organiza las citas en páginas correspondientes a un día específico.
    *   Permite buscar páginas por día y mes. Si la página no existe, se crea automáticamente.
    *   Las páginas se ordenan automáticamente por fecha (mes y día).
*   **Navegación:**
    *   Navegación entre páginas mediante los botones "Anterior" y "Siguiente".
    *   Posibilidad de ir directamente a una fecha específica mediante un diálogo.
*   **Gestión de Citas:**
    *   Agregar nuevas citas a una página específica, indicando título, descripción, hora y minuto.
    *   Eliminar citas existentes seleccionándolas en la tabla y pulsando el botón "Eliminar Cita".
    *   Buscar citas por hora en la página actual.
*   **Interfaz Gráfica:**
    *   Interfaz intuitiva con una tabla que muestra las citas de la página actual.
    *   Visualización de la fecha actual en la parte superior de la ventana.
    *   Icono de la aplicación y logo (opcional).
*   **Validación de Datos:**
    *   Validación de los datos ingresados al agregar una cita (por ejemplo, el título no puede estar vacío).
    *   Validación de los valores de día y mes al crear o buscar una página.

## Estructura del Proyecto

El proyecto se compone de las siguientes clases principales:

*   `Agenda`: Gestiona las páginas de la agenda, permitiendo buscar, crear y navegar entre ellas.
*   `Pagina`: Representa una página de la agenda correspondiente a un día específico, conteniendo una lista de citas.
*   `Cita`: Representa una cita con título, texto, hora y minuto.
*   `AgendaUI`: Clase principal que construye la interfaz gráfica de la aplicación.
*   `SistemaAgenda`: Clase que contiene el método `main` para ejecutar la aplicación.

## Cómo Ejecutar el Proyecto

1.  Asegúrate de tener instalado el JDK (Java Development Kit) en tu sistema.
2.  Compila los archivos `.java` utilizando un IDE como VS Code o la línea de comandos:

    ```
    javac src/*.java
    ```
3.  Ejecuta la clase principal `SistemaAgenda`:

    ```
    java SistemaAgenda
    ```

## Próximos Pasos

*   Implementar la edición de citas existentes.
*   Añadir la persistencia de datos para que las citas se guarden y se carguen al iniciar la aplicación.
*   Mejorar la interfaz gráfica con funcionalidades adicionales.

## Icono

Se ajustó el icono del formulario a un tamaño de 150x120 para una mejor visualización.
