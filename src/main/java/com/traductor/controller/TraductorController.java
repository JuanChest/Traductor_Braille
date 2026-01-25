package com.traductor.controller;

import com.traductor.model.ITraductor;
import com.traductor.service.GeneradorPDF;
import com.traductor.service.LectorPDF;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controlador FXML que gestiona la interacción entre la vista y el modelo.
 *
 * @see ITraductor
 */
public class TraductorController {
    @FXML
    private TextArea textoEntrada;

    @FXML
    private TextArea textoSalida;

    @FXML
    private CheckBox checkModoEspejo;

    @FXML
    private TextArea textoTraduccionPDF;

    @FXML
    private Label lblArchivoSeleccionado;

    private ITraductor traductor;

    /**
     * Constructor sin argumentos requerido por FXML.
     */
    public TraductorController() {
    }

    /**
     * Establece el traductor a utilizar.
     *
     * @param traductor El traductor a utilizar.
     */
    public void setTraductor(ITraductor traductor) {
        this.traductor = traductor;
    }

    /**
     * Método de inicialización llamado automáticamente por FXML.
     */
    @FXML
    private void initialize() {
        // Inicialización adicional si es necesaria
    }

    /**
     * Procesa la traducción solicitada por el usuario.
     * Este método es invocado cuando se hace clic en el botón "Traducir a Braille".
     */
    @FXML
    private void traducir() {
        String texto = textoEntrada.getText();

        if (texto == null || texto.trim().isEmpty()) {
            textoSalida.setText("Por favor ingrese texto para traducir");
            return;
        }

        String resultado = traductor.traducir(texto);
        textoSalida.setText(resultado);
    }

    /**
     * Limpia el texto de ambos campos (entrada y salida).
     * Este método es invocado cuando se hace clic en el botón "Limpiar Texto".
     */
    @FXML
    private void limpiarTexto() {
        textoEntrada.clear();
        textoSalida.clear();
    }

    /**
     * Genera y descarga un archivo PDF con la traducción a Braille.
     * Este método es invocado cuando se hace clic en el botón "Descargar PDF".
     */
    @FXML
    private void descargarPDF() {
        String textoOriginal = textoEntrada.getText();
        String textoBraille = textoSalida.getText();

        // Validar que hay contenido para exportar
        if (textoBraille == null || textoBraille.trim().isEmpty() ||
                textoBraille.equals("Por favor ingrese texto para traducir")) {
            mostrarNotificacionMinimalista("Por favor traduce un texto antes de descargar", "warning");
            return;
        }

        // Crear diálogo para guardar archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar PDF");

        // Configurar nombre de archivo por defecto con fecha y hora
        String nombreArchivo = "traduccion_braille_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                ".pdf";
        fileChooser.setInitialFileName(nombreArchivo);

        // Filtro para archivos PDF
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        // Mostrar diálogo y obtener archivo seleccionado
        File archivo = fileChooser.showSaveDialog(textoSalida.getScene().getWindow());

        if (archivo != null) {
            try {
                // Verificar si el modo espejo está activado
                boolean modoEspejo = checkModoEspejo != null && checkModoEspejo.isSelected();
                
                // Generar el PDF con o sin modo espejo
                GeneradorPDF.generarPDF(textoOriginal, textoBraille, archivo.getAbsolutePath(), modoEspejo);

                // Mostrar notificación minimalista de éxito
                mostrarNotificacionMinimalista("PDF generado correctamente", "success");

            } catch (Exception e) {
                // Mostrar notificación minimalista de error
                mostrarNotificacionMinimalista("Error al generar el PDF", "error");
                e.printStackTrace();
            }
        }
    }

    /**
     * Muestra una notificación minimalista que desaparece automáticamente.
     *
     * @param mensaje El mensaje a mostrar
     * @param tipo    El tipo de mensaje (success, error, warning)
     */
    private void mostrarNotificacionMinimalista(String mensaje, String tipo) {
        Stage notificacion = new Stage();
        notificacion.initStyle(StageStyle.UNDECORATED);
        notificacion.initOwner(textoSalida.getScene().getWindow());

        Label label = new Label(mensaje);
        label.setStyle(
                "-fx-padding: 15px 25px; " +
                        "-fx-background-color: " + obtenerColorFondo(tipo) + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        VBox vbox = new VBox(label);
        vbox.setStyle("-fx-background-color: transparent;");

        javafx.scene.Scene scene = new javafx.scene.Scene(vbox);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        notificacion.setScene(scene);

        // Obtener la ventana principal
        Stage ventanaPrincipal = (Stage) textoSalida.getScene().getWindow();

        // Mostrar la notificación primero para obtener sus dimensiones
        notificacion.show();

        // Posicionar en la esquina superior derecha de la ventana principal
        double x = ventanaPrincipal.getX() + ventanaPrincipal.getWidth() - notificacion.getWidth() - 20;
        double y = ventanaPrincipal.getY() + 20;

        notificacion.setX(x);
        notificacion.setY(y);

        // Cerrar automáticamente después de 2 segundos
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> notificacion.close());
        delay.play();
    }

    /**
     * Permite seleccionar un archivo PDF con texto Braille para traducir a español.
     * Este método es invocado cuando se hace clic en el botón "Seleccionar Archivo PDF".
     */
    @FXML
    private void seleccionarArchivoPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar PDF con texto Braille");
        
        // Filtro para archivos PDF
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        
        // Mostrar diálogo para abrir archivo
        File archivoPDF = fileChooser.showOpenDialog(textoTraduccionPDF.getScene().getWindow());
        
        if (archivoPDF != null) {
            try {
                // Actualizar label con el nombre del archivo
                lblArchivoSeleccionado.setText(archivoPDF.getName());
                lblArchivoSeleccionado.setStyle("-fx-font-style: normal; -fx-text-fill: #2196F3; -fx-font-weight: bold;");
                
                // Leer el PDF y traducir de Braille a Español
                String textoBraille = LectorPDF.leerPDF(archivoPDF.getAbsolutePath());
                String textoEspanol = traductor.traducirBrailleAEspanol(textoBraille);
                
                // Mostrar la traducción
                textoTraduccionPDF.setText(textoEspanol);
                
                // Mostrar notificación de éxito
                mostrarNotificacionMinimalista("PDF leído correctamente", "success");
                
            } catch (Exception e) {
                lblArchivoSeleccionado.setText("Error al leer el archivo");
                lblArchivoSeleccionado.setStyle("-fx-font-style: italic; -fx-text-fill: #F44336;");
                textoTraduccionPDF.setText("Error al procesar el archivo PDF: " + e.getMessage());
                mostrarNotificacionMinimalista("Error al leer el PDF", "error");
                e.printStackTrace();
            }
        }
    }

    /**
     * Obtiene el color de fondo según el tipo de notificación.
     * 
     * @param tipo El tipo de notificación (success, error, warning)
     * @return El color de fondo en formato hexadecimal.
     */
    private String obtenerColorFondo(String tipo) {
        switch (tipo) {
            case "success":
                return "#4CAF50";
            case "error":
                return "#F44336";
            case "warning":
                return "#FF9800";
            default:
                return "#2196F3";
        }
    }
}
