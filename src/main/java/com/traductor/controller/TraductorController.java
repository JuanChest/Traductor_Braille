package com.traductor.controller;

import com.traductor.model.ITraductor;
import com.traductor.service.GeneradorPDF;

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

    // Campos para la celda Braille interactiva
    @FXML
    private CheckBox chkPunto1;
    @FXML
    private CheckBox chkPunto2;
    @FXML
    private CheckBox chkPunto3;
    @FXML
    private CheckBox chkPunto4;
    @FXML
    private CheckBox chkPunto5;
    @FXML
    private CheckBox chkPunto6;

    @FXML
    private TextArea textoBrailleIngresado;

    @FXML
    private TextArea textoResultadoEspanol;

    @FXML
    private javafx.scene.layout.VBox containerPestanaBraille;

    // Estado interno para los puntos de la celda Braille
    private boolean[] puntosActivos = new boolean[6];
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
        configurarEventosTeclado();
        configurarEventosCheckboxes();
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
     * Configura los eventos de teclado para la pestaña de entrada Braille
     * interactiva.
     */
    private void configurarEventosTeclado() {
        if (containerPestanaBraille != null) {
            containerPestanaBraille.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    // Teclas numéricas alfanuméricos
                    case DIGIT1:
                        togglePunto(0);
                        break;
                    case DIGIT2:
                        togglePunto(1);
                        break;
                    case DIGIT3:
                        togglePunto(2);
                        break;
                    case DIGIT4:
                        togglePunto(3);
                        break;
                    case DIGIT5:
                        togglePunto(4);
                        break;
                    case DIGIT6:
                        togglePunto(5);
                        break;

                    // Teclas numéricas del teclado numérico
                    case NUMPAD1:
                        togglePunto(0);
                        break;
                    case NUMPAD2:
                        togglePunto(1);
                        break;
                    case NUMPAD3:
                        togglePunto(2);
                        break;
                    case NUMPAD4:
                        togglePunto(3);
                        break;
                    case NUMPAD5:
                        togglePunto(4);
                        break;
                    case NUMPAD6:
                        togglePunto(5);
                        break;

                    // Teclas de control
                    case ENTER:
                        confirmarSimbolo();
                        break;
                    case SPACE:
                        insertarEspacio();
                        break;
                    case BACK_SPACE:
                        borrarUltimoSimbolo();
                        break;

                    default:
                        break;
                }
                event.consume();
            });

            // Solicitar foco cuando se hace clic en el container
            containerPestanaBraille.setOnMouseClicked(e -> containerPestanaBraille.requestFocus());
        }
    }

    /**
     * Configura los eventos de clic en los checkboxes de la celda Braille.
     * Permite que el usuario active/desactive puntos con el mouse.
     */
    private void configurarEventosCheckboxes() {
        CheckBox[] checkboxes = { chkPunto1, chkPunto2, chkPunto3, chkPunto4, chkPunto5, chkPunto6 };

        for (int i = 0; i < checkboxes.length; i++) {
            final int indice = i;
            if (checkboxes[i] != null) {
                checkboxes[i].setOnAction(event -> {
                    // Sincronizar el estado del checkbox con el array interno
                    puntosActivos[indice] = checkboxes[indice].isSelected();
                });
            }
        }
    }

    /**
     * Activa o desactiva un punto en la celda Braille.
     * 
     * @param indice Índice del punto (0-5 correspondiente a puntos 1-6)
     */
    private void togglePunto(int indice) {
        if (indice < 0 || indice >= 6)
            return;

        puntosActivos[indice] = !puntosActivos[indice];

        // Actualizar el checkbox visual correspondiente
        CheckBox[] checkboxes = { chkPunto1, chkPunto2, chkPunto3, chkPunto4, chkPunto5, chkPunto6 };
        if (checkboxes[indice] != null) {
            checkboxes[indice].setSelected(puntosActivos[indice]);
        }
    }

    /**
     * Confirma el símbolo Braille actual y lo añade al TextArea de entrada.
     * Este método es llamado cuando se presiona ENTER.
     */
    private void confirmarSimbolo() {
        String patron = obtenerPatronActual();
        char simboloBraille = convertirPatronABrailleUnicode(patron);

        if (textoBrailleIngresado != null) {
            textoBrailleIngresado.appendText(String.valueOf(simboloBraille));
        }

        limpiarCelda();
    }

    /**
     * Inserta un espacio Braille (U+2800) en el TextArea de entrada.
     * Este método es llamado cuando se presiona ESPACIO.
     */
    private void insertarEspacio() {
        if (textoBrailleIngresado != null) {
            textoBrailleIngresado.appendText("\u2800"); // Espacio Braille Unicode
        }
        limpiarCelda();
    }

    /**
     * Borra el último símbolo del TextArea de entrada Braille.
     * Este método es llamado cuando se presiona BACKSPACE.
     */
    private void borrarUltimoSimbolo() {
        if (textoBrailleIngresado != null) {
            String texto = textoBrailleIngresado.getText();
            if (!texto.isEmpty()) {
                textoBrailleIngresado.setText(texto.substring(0, texto.length() - 1));
            }
        }
    }

    /**
     * Obtiene el patrón de puntos actualmente activados.
     * 
     * @return String con los números de los puntos activos (ej: "145")
     */
    private String obtenerPatronActual() {
        StringBuilder patron = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            if (puntosActivos[i]) {
                patron.append(i + 1); // Los puntos se numeran 1-6
            }
        }
        return patron.length() > 0 ? patron.toString() : "0";
    }

    /**
     * Convierte un patrón de puntos a un carácter Braille Unicode.
     * 
     * @param patron Patrón de puntos (ej: "145")
     * @return Carácter Braille Unicode correspondiente
     */
    private char convertirPatronABrailleUnicode(String patron) {
        int valor = 0x2800; // Valor base de Braille Unicode

        if (patron.equals("0")) {
            return (char) valor; // Espacio Braille vacío
        }

        for (char c : patron.toCharArray()) {
            int punto = c - '0'; // Convertir char a número
            switch (punto) {
                case 1:
                    valor += 0x01;
                    break;
                case 2:
                    valor += 0x02;
                    break;
                case 3:
                    valor += 0x04;
                    break;
                case 4:
                    valor += 0x08;
                    break;
                case 5:
                    valor += 0x10;
                    break;
                case 6:
                    valor += 0x20;
                    break;
            }
        }

        return (char) valor;
    }

    /**
     * Limpia todos los puntos de la celda Braille.
     */
    private void limpiarCelda() {
        for (int i = 0; i < 6; i++) {
            puntosActivos[i] = false;
        }

        // Actualizar checkboxes visuales
        CheckBox[] checkboxes = { chkPunto1, chkPunto2, chkPunto3, chkPunto4, chkPunto5, chkPunto6 };
        for (CheckBox chk : checkboxes) {
            if (chk != null) {
                chk.setSelected(false);
            }
        }
    }

    /**
     * Traduce los símbolos Braille ingresados a Español.
     * Este método es invocado cuando se hace clic en el botón "Traducir a Español".
     */
    @FXML
    private void traducirBrailleIngresado() {
        if (textoBrailleIngresado == null || textoResultadoEspanol == null)
            return;

        String braille = textoBrailleIngresado.getText();
        if (braille.isEmpty()) {
            textoResultadoEspanol.setText("Ingresa símbolos Braille primero");
            return;
        }

        String resultado = traductor.traducirBrailleAEspanol(braille);
        textoResultadoEspanol.setText(resultado);
    }

    /**
     * Limpia todos los campos de la entrada Braille interactiva.
     * Este método es invocado cuando se hace clic en el botón "Limpiar".
     */
    @FXML
    private void limpiarBrailleIngresado() {
        if (textoBrailleIngresado != null) {
            textoBrailleIngresado.clear();
        }
        if (textoResultadoEspanol != null) {
            textoResultadoEspanol.clear();
        }
        limpiarCelda();
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
